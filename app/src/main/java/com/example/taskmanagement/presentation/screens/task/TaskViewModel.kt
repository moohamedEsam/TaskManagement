package com.example.taskmanagement.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.shared.RemoveMembersUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskStatusUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.CreateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.DeleteCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.UpdateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.CreateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.DeleteTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.UpdateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val updateTaskItemsUseCase: UpdateTaskItemsUseCase,
    private val createTaskItemsUseCase: CreateTaskItemsUseCase,
    private val deleteTaskItemsUseCase: DeleteTaskItemsUseCase,
    private val removeMembersUseCase: RemoveMembersUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val validator: BaseValidator,
    private val taskId: String
) : ViewModel() {
    private val _task = MutableStateFlow<Resource<TaskView>>(Resource.Initialized())
    val task = _task.asStateFlow()
    private val _taskItems = MutableStateFlow(emptySet<TaskItem>())
    val taskItems = _taskItems.asStateFlow()
    private val _comments = MutableStateFlow(emptySet<CommentView>())
    val comments = _comments.asStateFlow()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    private val _showTaskStatusDialog = MutableStateFlow(false)
    val showTaskStatusDialog = _showTaskStatusDialog.asStateFlow()
    private val uiEvents = MutableStateFlow(emptySet<TaskScreenUIEvent>())
    val uiEventState = uiEvents.asStateFlow()
    private val _updateMade = MutableStateFlow(false)
    val updateMade = _updateMade.asStateFlow()
    private val _taskItemTitleValidationResult = MutableStateFlow(ValidationResult(true))
    val taskItemTitleValidationResult = _taskItemTitleValidationResult.asStateFlow()
    private val _isUpdateAllowed = MutableStateFlow(false)
    val isUpdateAllowed = _isUpdateAllowed.asStateFlow()
    var currentUser = User("")

    init {
        getTask()
        setUserId()
        setUpdateMade()
        setIsUpdateAllowed()
    }

    private fun setUpdateMade() = viewModelScope.launch {
        uiEvents.collectLatest {
            _updateMade.update { _ -> it.isNotEmpty() }
        }
    }

    private fun setIsUpdateAllowed() = viewModelScope.launch {
        task.collectLatest { taskRes ->
            _isUpdateAllowed.update {
                taskRes.data?.status != TaskStatus.Completed &&
                        taskRes.data?.status != TaskStatus.Pending &&
                        taskRes.data?.status != TaskStatus.Canceled
            }
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
            currentUser = it
        }
        result.onError {
            val snackBarEvent = SnackBarEvent(it ?: "") { setUserId() }
            snackBarChannel.send(snackBarEvent)
        }
    }


    fun onTaskStatusClick(showSnackBarOnError: Boolean = true): Job = viewModelScope.launch {
        task.value.onSuccess {
            val exist =
                it.assigned.find { activeUser -> activeUser.id == currentUser.id } != null
            if (!exist) {
                if (!showSnackBarOnError) return@launch
                val event =
                    SnackBarEvent("only assigned members can change task status", null) {}
                snackBarChannel.send(event)
                return@launch
            }
            if (it.status == TaskStatus.Pending)
                setShowTaskStatusDialog(true)
            else if (it.status != TaskStatus.Completed && _taskItems.value.all { item -> item.isCompleted })
                setShowTaskStatusDialog(true)

        }
    }

    fun validateTaskItemTitle(value: String) {
        _taskItemTitleValidationResult.update { validator.nameValidator.validate(value) }
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
                    taskView.assigned.remove(uiEvent.user)
                    _task.update { Resource.Success(taskView) }
                }
                is TaskScreenUIEvent.StatusChanged -> {
                    toggleTaskStatus()
                }
                is TaskScreenUIEvent.Comments.Add -> {
                    uiEvent.comment.issuer = currentUser
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


    private fun toggleTaskStatus() = viewModelScope.launch {
        _task.update {
            var taskView = it.data ?: return@launch
            if (taskView.status == TaskStatus.Pending)
                taskView = taskView.copy(status = TaskStatus.InProgress)
            else if (isUpdateAllowed.value && _taskItems.value.all { item -> item.isCompleted })
                taskView = taskView.copy(status = TaskStatus.Completed)

            Resource.Success(taskView)
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
                    taskView.assigned.add(uiEvent.user)
                    _task.update { Resource.Success(taskView) }
                }
                is TaskScreenUIEvent.StatusChanged -> {
                    _task.update {
                        if (taskView.status == TaskStatus.InProgress)
                            it.copy(taskView.copy(status = TaskStatus.Pending))
                        else
                            it.copy(taskView.copy(status = TaskStatus.InProgress))
                    }
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
            undoLastEvent().join()
    }

    fun optimizeEvents() = viewModelScope.launch {
        for (event in uiEvents.value) {
            when (event) {
                is TaskScreenUIEvent.TaskItems.Edit -> {
                    val addEvent =
                        uiEvents.value.find { it is TaskScreenUIEvent.TaskItems.Add && it.taskItem == event.oldTaskItem } as? TaskScreenUIEvent.TaskItems.Add
                            ?: continue
                    uiEvents.update { it - event - addEvent + TaskScreenUIEvent.TaskItems.Add(event.taskItem) }
                }
                is TaskScreenUIEvent.TaskItems.Remove -> {
                    val addEvent =
                        uiEvents.value.find { it is TaskScreenUIEvent.TaskItems.Add && it.taskItem == event.taskItem } as? TaskScreenUIEvent.TaskItems.Add
                            ?: continue
                    uiEvents.update { it - event - addEvent }
                }
                is TaskScreenUIEvent.Comments.Edit -> {
                    val addEvent =
                        uiEvents.value.find { it is TaskScreenUIEvent.Comments.Add && it.comment == event.oldComment } as? TaskScreenUIEvent.Comments.Add
                            ?: continue
                    uiEvents.update { it - event - addEvent + TaskScreenUIEvent.Comments.Add(event.comment) }
                }
                is TaskScreenUIEvent.Comments.Remove -> {
                    val addEvent =
                        uiEvents.value.find { it is TaskScreenUIEvent.Comments.Add && it.comment == event.comment } as? TaskScreenUIEvent.Comments.Add
                            ?: continue
                    uiEvents.update { it - event - addEvent }
                }
                else -> continue
            }
        }
    }

    fun saveChanges(): Job = viewModelScope.launch {
        optimizeEvents()
        val createdComments = mutableListOf<Comment>()
        val createdTaskItems = mutableListOf<TaskItem>()
        val deletedMembers = mutableListOf<String>()
        val updatedTaskItems = mutableListOf<String>()
        var statusChanged = false
        for (event in uiEvents.value) {
            when (event) {
                is TaskScreenUIEvent.Comments.Add -> createdComments.add(event.comment.toComment())
                is TaskScreenUIEvent.Comments.Edit -> updateCommentUseCase(event.comment.toComment())
                is TaskScreenUIEvent.Comments.Remove -> deleteCommentUseCase(event.comment.id)
                is TaskScreenUIEvent.MembersRemove -> deletedMembers.add(event.user.id)
                TaskScreenUIEvent.StatusChanged -> statusChanged = true
                is TaskScreenUIEvent.TaskItems.Add -> createdTaskItems.add(event.taskItem)
                is TaskScreenUIEvent.TaskItems.Edit -> updatedTaskItems.add(event.taskItem.id)
                is TaskScreenUIEvent.TaskItems.Remove -> deleteTaskItemsUseCase(event.taskItem.id)
            }
        }
        if (createdTaskItems.isNotEmpty())
            createTaskItemsUseCase(createdTaskItems)
        if (createdComments.isNotEmpty())
            createCommentUseCase(createdComments)
        if (deletedMembers.isNotEmpty())
            removeMembersUseCase(
                RemoveMembersUseCase.Params(
                    taskId,
                    ParentRoute.Tasks,
                    deletedMembers
                )
            )
        if (updatedTaskItems.isNotEmpty())
            updateTaskItemsUseCase(updatedTaskItems)
        if (statusChanged)
            updateTaskStatusUseCase(task.value.data?.id ?: "")
        uiEvents.update { emptySet() }
    }


    fun setShowTaskStatusDialog(value: Boolean) {
        _showTaskStatusDialog.update { value }
    }
}

