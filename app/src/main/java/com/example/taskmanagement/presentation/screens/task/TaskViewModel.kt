package com.example.taskmanagement.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.useCases.shared.RemoveMembersUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.CreateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.UpdateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.UpdateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val updateTaskItemsUseCase: UpdateTaskItemsUseCase,
    private val removeMembersUseCase: RemoveMembersUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val taskId: String
) : ViewModel() {
    private val _task = MutableStateFlow<Resource<TaskView>>(Resource.Initialized())
    val task = _task.asStateFlow()
    private val _taskItems = MutableStateFlow(emptySet<TaskItem>())
    val taskItems = _taskItems.asStateFlow()
    private val _comments = MutableStateFlow(emptySet<Comment>())
    val comments = _comments.asStateFlow()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    private val _showTaskStatusDialog = MutableStateFlow(false)
    val showTaskStatusDialog = _showTaskStatusDialog.asStateFlow()
    private val uiEvents = MutableStateFlow(emptyList<TaskScreenUIEvent>())
    val uiEventState = uiEvents.asStateFlow()
    private val _updateMade = MutableStateFlow(false)
    val updateMade = _updateMade.asStateFlow()

    private var userId = ""

    init {
        getTask()
        setUserId()
        setUpdateMade()
    }

    private fun setUpdateMade() = viewModelScope.launch {
        uiEvents.collectLatest {
            _updateMade.update { _ -> it.isNotEmpty() }
        }
    }


    fun getTask(): Job = viewModelScope.launch {
        _task.update { getTaskUseCase(taskId) }
        task.value.onError {
            val snackBarEvent = SnackBarEvent(it ?: "") { getTask() }
            snackBarChannel.send(snackBarEvent)
        }
        task.value.onSuccess {
            _taskItems.update { _ -> it.taskItems.toSet() }
            _comments.update { _ -> it.comments.toSet() }
        }
    }


    fun setUserId(): Job = viewModelScope.launch {
        val result = getCurrentUserProfileUseCase(Unit)
        result.onSuccess {
            userId = it.id
        }
        result.onError {
            val snackBarEvent = SnackBarEvent(it ?: "") { setUserId() }
            snackBarChannel.send(snackBarEvent)
        }
    }


    fun onTaskStatusClick(showEvent: Boolean = true): Job = viewModelScope.launch {
        task.value.onSuccess {
            val exist = it.assigned.find { activeUser -> activeUser.user.id == userId } != null
            if (!exist) {
                if (!showEvent) return@launch
                val event =
                    SnackBarEvent("only assigned members can change task status", null) {}
                snackBarChannel.send(event)
                return@launch
            }
            if (it.status == TaskStatus.Pending)
                setShowTaskStatusDialog(true)
            else if (it.status == TaskStatus.InProgress && it.taskItems.all { item -> item.completed })
                setShowTaskStatusDialog(true)

        }
    }


    fun addEventUI(uiEvent: TaskScreenUIEvent): Job = viewModelScope.launch {
        task.value.onSuccess { taskView ->
            when (uiEvent) {
                is TaskScreenUIEvent.TaskItems.Add -> {
                    _taskItems.update { it + uiEvent.taskItem }
                }
                is TaskScreenUIEvent.TaskItems.Edit -> {
                    _taskItems.update { it + uiEvent.taskItem - uiEvent.oldTaskItem }
                }
                is TaskScreenUIEvent.TaskItems.Remove -> {
                    _taskItems.update { it - uiEvent.taskItem }
                }
                is TaskScreenUIEvent.MembersRemove -> {
                    taskView.assigned.remove(uiEvent.activeUserDto)
                    _task.update { Resource.Success(taskView) }
                }
                is TaskScreenUIEvent.StatusChanged -> {
                    toggleTaskStatus()
                }
                is TaskScreenUIEvent.Comments.Add -> {
                    _comments.update { it + uiEvent.comment }
                }
                is TaskScreenUIEvent.Comments.Edit -> {
                    _comments.update { it + uiEvent.comment - uiEvent.oldComment }
                }
                is TaskScreenUIEvent.Comments.Remove -> {
                    _comments.update { it - uiEvent.comment }
                }
            }
            uiEvents.update { e -> e + uiEvent }
        }

    }


    fun onTaskStatusConfirmClick() = viewModelScope.launch {
        toggleTaskStatus()
        setShowTaskStatusDialog(false)
        uiEvents.update { it + TaskScreenUIEvent.StatusChanged }
    }


    private fun toggleTaskStatus() {
        _task.update {
            val taskView = it.data ?: return
            when (taskView.status) {
                TaskStatus.Pending -> it.copy(taskView.copy(status = TaskStatus.InProgress))
                TaskStatus.InProgress -> it.copy(taskView.copy(status = TaskStatus.Completed))
                else -> it
            }
        }
    }

    fun undoLastEvent() = viewModelScope.launch {
        if (uiEvents.value.isEmpty())
            return@launch
        val uiEvent = uiEvents.value.last()
        task.value.onSuccess { taskView ->
            when (uiEvent) {
                is TaskScreenUIEvent.TaskItems.Add -> {
                    _taskItems.update { it - uiEvent.taskItem }
                }
                is TaskScreenUIEvent.TaskItems.Edit -> {
                    _taskItems.update { it - uiEvent.taskItem + uiEvent.oldTaskItem }
                }
                is TaskScreenUIEvent.TaskItems.Remove -> {
                    _taskItems.update { it + uiEvent.taskItem }
                }
                is TaskScreenUIEvent.MembersRemove -> {
                    taskView.assigned.add(uiEvent.activeUserDto)
                    _task.update { Resource.Success(taskView) }
                }
                is TaskScreenUIEvent.StatusChanged -> {
                    toggleTaskStatus()
                }
                is TaskScreenUIEvent.Comments.Add -> {
                    _comments.update { it - uiEvent.comment }
                }
                is TaskScreenUIEvent.Comments.Edit -> {
                    _comments.update { it - uiEvent.comment + uiEvent.oldComment }
                }
                is TaskScreenUIEvent.Comments.Remove -> {
                    _comments.update { it + uiEvent.comment }
                }
            }
            uiEvents.update { it - uiEvent }
        }
    }

    fun discardChanges() = viewModelScope.launch {
        while (uiEvents.value.isNotEmpty())
            undoLastEvent()
    }

    fun saveChanges(): Job = viewModelScope.launch {
        task.value.onSuccess {
            val errorMessages = mutableListOf<String>()
            val eventGroups = uiEvents.value.groupBy { eventType -> eventType }
            for ((event, values) in eventGroups) {
                when (event) {
                    is TaskScreenUIEvent.TaskItems -> {
                        val result = updateTaskItemsUseCase(
                            UpdateTaskItemsUseCase.Params(
                                taskId,
                                taskItems.value.toList()
                            )
                        )
                        result.onError { message ->
                            errorMessages.add(message ?: "")
                        }
                    }
                    is TaskScreenUIEvent.MembersRemove -> {
                        val ids =
                            (values as List<TaskScreenUIEvent.MembersRemove>).map { e -> e.activeUserDto.user.id }
                        val result = removeMembersUseCase(
                            RemoveMembersUseCase.Params(
                                taskId,
                                ParentRoute.Tasks,
                                ids
                            )
                        )
                        result.onError { message ->
                            errorMessages.add(message ?: "")
                        }
                    }
                    is TaskScreenUIEvent.Comments.Add -> {
                        val commentIds =
                            (values as List<TaskScreenUIEvent.Comments.Add>).map { c -> c.comment.id }
                        val comments =
                            it.comments.filter { comment -> commentIds.contains(comment.id) }
                        val result = createCommentUseCase(comments)
                        result.onError { message ->
                            errorMessages.add(message ?: "")
                        }
                        result.onSuccess { commentsResponse ->

                        }
                    }
                    is TaskScreenUIEvent.Comments.Edit -> {

                    }
                    is TaskScreenUIEvent.Comments.Remove -> {}
                    else -> {
                        task.value.onSuccess { taskView ->
                            updateTaskUseCase(taskView.toTask())
                        }
                    }
                }
            }

        }
    }


    private fun setShowTaskStatusDialog(value: Boolean) {
        _showTaskStatusDialog.update { value }
    }

    fun showEditButton() = true
}