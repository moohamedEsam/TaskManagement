package com.example.taskmanagement.presentation.screens.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.shared.RemoveMembersUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.CreateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.UpdateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.UpdateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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
    val task = mutableStateOf<Resource<TaskView>>(Resource.Initialized())
    val taskItems = mutableStateListOf<TaskItem>()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    val showTaskStatusDialog = mutableStateOf(false)
    private val uiEvents = mutableListOf<TaskScreenUIEvent>()
    private var userId = ""

    init {
        getTask()
        setUserId()
    }


    private fun getTask() {
        viewModelScope.launch {
            task.value = getTaskUseCase(taskId)
            task.value.onError {
                val snackBarEvent = SnackBarEvent(it ?: "") { getTask() }
                snackBarChannel.send(snackBarEvent)
            }
            task.value.onSuccess {
                val comment = Comment(
                    "",
                    "hi this is the first comment @mohamedEsam",
                    listOf("mohamedEsam"),
                    id = ""
                )
                task.value = task.value.copy(it.copy(comments = mutableListOf(comment)))
                taskItems.addAll(it.taskItems)
            }
        }
    }

    private fun setUserId() {
        viewModelScope.launch {
            val result = getCurrentUserProfileUseCase(Unit)
            result.onSuccess {
                userId = it.id
            }
            result.onError {
                val snackBarEvent = SnackBarEvent(it ?: "") { setUserId() }
                snackBarChannel.send(snackBarEvent)
            }
        }
    }

    fun onTaskStatusClick() {
        viewModelScope.launch {
            task.value.onSuccess {
                if (it.assigned.find { activeUser -> activeUser.user.id == userId } == null) {
                    val event =
                        SnackBarEvent("only assigned members can change task status", null) {}
                    snackBarChannel.send(event)
                } else {
                    if (it.status == TaskStatus.Pending)
                        toggleTaskStatusDialog()
                    else if (it.status == TaskStatus.InProgress && it.taskItems.all { item -> item.completed })
                        toggleTaskStatusDialog()
                }
            }

        }
    }

    fun addEventUI(uiEvent: TaskScreenUIEvent) {
        viewModelScope.launch {
            task.value.onSuccess {
                when (uiEvent) {
                    is TaskScreenUIEvent.TaskItemToggle -> {
                        handleTaskItemToggle(uiEvent)
                    }
                    is TaskScreenUIEvent.MembersRemove -> {
                        handleRemoveMembers(it, uiEvent)
                    }
                    is TaskScreenUIEvent.StatusChanged -> {
                        toggleTaskStatus()
                        uiEvents.add(uiEvent)
                    }
                    is TaskScreenUIEvent.Comments.Add -> {
                        it.comments.add(uiEvent.comment)
                        task.value = task.value.copy()
                        uiEvents.add(uiEvent)
                    }
                    is TaskScreenUIEvent.Comments.Edit -> {
                        handleCommentEdit(it, uiEvent)
                        handleCommentAddedThenUpdated(uiEvent)
                        uiEvents.add(TaskScreenUIEvent.Comments.Add(uiEvent.comment))
                    }
                    is TaskScreenUIEvent.Comments.Remove -> {
                        it.comments.remove(uiEvent.comment)
                        handleCommentAddedThenUpdated(uiEvent)
                        task.value = task.value.copy()
                    }
                }

            }
        }
    }

    private fun CoroutineScope.handleCommentAddedThenUpdated(uiEvent: TaskScreenUIEvent.Comments) {
        launch {
            val addEvent =
                uiEvents.find { e -> e is TaskScreenUIEvent.Comments.Add && e.comment.id == uiEvent.comment.id }

            if (addEvent == null)
                uiEvents.add(uiEvent)
            else {
                uiEvents.remove(addEvent)
            }
        }
    }

    private fun handleCommentEdit(
        taskView: TaskView,
        uiEvent: TaskScreenUIEvent.Comments.Edit
    ) {
        taskView.comments.removeIf { it.id == uiEvent.comment.id }
        taskView.comments.add(uiEvent.comment)
        task.value = task.value.copy()
    }

    private suspend fun handleRemoveMembers(
        it: TaskView,
        uiEvent: TaskScreenUIEvent.MembersRemove
    ) {
        it.assigned.remove(uiEvent.activeUserDto)
        task.value = task.value.copy()
        uiEvents.add(uiEvent)
        val event = SnackBarEvent(
            "${uiEvent.activeUserDto.user.username} was removed",
            "Undo"
        ) {
            it.assigned.add(uiEvent.activeUserDto)
            task.value = task.value.copy()
            uiEvents.remove(uiEvent)
        }
        snackBarChannel.send(event)
    }

    private fun CoroutineScope.handleTaskItemToggle(uiEvent: TaskScreenUIEvent.TaskItemToggle) {
        taskItems.apply {
            remove(uiEvent.taskItem)
            add(uiEvent.taskItem.copy(completed = !uiEvent.taskItem.completed))
        }
        launch {
            val exist =
                uiEvents.removeIf { it is TaskScreenUIEvent.TaskItemToggle && it.taskItem.id == uiEvent.taskItem.id }
            if (!exist)
                uiEvents.add(uiEvent)
        }
    }

    fun onTaskStatusConfirmClick() {
        viewModelScope.launch {
            toggleTaskStatus()
            toggleTaskStatusDialog()
            uiEvents.add(TaskScreenUIEvent.StatusChanged)
        }
    }

    private fun toggleTaskStatus() {
        task.value.onSuccess {
            if (it.status == TaskStatus.Pending)
                task.value = task.value.copy(it.copy(status = TaskStatus.InProgress))
            else if (it.status == TaskStatus.InProgress)
                task.value = task.value.copy(it.copy(status = TaskStatus.Completed))
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            task.value.onSuccess {
                val errorMessages = mutableListOf<String>()
                val eventGroups = uiEvents.groupBy { eventType -> eventType }
                for ((event, values) in eventGroups) {
                    when (event) {
                        is TaskScreenUIEvent.TaskItemToggle -> {
                            val result = updateTaskItemsUseCase(
                                UpdateTaskItemsUseCase.Params(
                                    taskId,
                                    taskItems
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
                                task.value =
                                    task.value.copy(it.copy(comments = commentsResponse.toMutableList()))
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
    }

    fun toggleTaskStatusDialog() {
        showTaskStatusDialog.value = !showTaskStatusDialog.value
    }

    fun showEditButton() = true
}