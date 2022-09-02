package com.example.taskmanagement.presentation.screens.forms.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.projects.GetCurrentUserProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.tasks.CreateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TaskFormViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val getCurrentUserTag: GetCurrentUserTag,
    private val getCurrentUserProjectUseCase: GetCurrentUserProjectUseCase,
    private val getProjectUseCase: GetProjectUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    projectId: String,
    private val taskId: String
) : ViewModel() {
    val project = mutableStateOf<Resource<ProjectView>>(Resource.Initialized())
    val projects = mutableStateOf<Resource<List<Project>>>(Resource.Initialized())
    val isUpdating = taskId.isNotBlank()
    private var currentUserTag: Tag? = null
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    val assigned = mutableStateListOf<String>()
    val taskView = mutableStateOf(getInitializedTaskView())
    val taskItems = mutableStateListOf<TaskItem>()

    init {
        viewModelScope.launch {
            if (projectId.isNotBlank())
                assignProject(projectId)
            if (isUpdating) {
                setTask(taskId)
            }
        }
    }

    private suspend fun setTask(taskId: String) {
        val result = getTaskUseCase(taskId)
        result.onSuccess {
            taskView.value = it
            assigned.addAll(it.assigned.map { activeUser -> activeUser.user.id })
            taskItems.addAll(it.taskItems)
        }

        result.onError {
            val event = SnackBarEvent(it ?: "") { setTask(taskId) }
            snackBarChannel.send(event)
        }
    }

    private fun getCurrentUserTag() {
        viewModelScope.launch {
            val result = getCurrentUserTag(
                GetCurrentUserTag.Params(
                    ParentRoute.Projects,
                    project.value.data?.id ?: ""
                )
            )
            result.onSuccess {
                currentUserTag = it
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { getCurrentUserTag() }
                snackBarChannel.send(event)
            }
        }
    }

    fun getProjects() {
        viewModelScope.launch {
            projects.value = getCurrentUserProjectUseCase(Unit)
            projects.value.onError {
                val event = SnackBarEvent(it ?: "") { getProjects() }
                snackBarChannel.send(event)
            }
        }
    }

    fun setProject(id: String) {
        viewModelScope.launch {
            assignProject(id)
        }
    }

    private suspend fun assignProject(id: String) {
        project.value = getProjectUseCase(id)
        project.value.onSuccess {
            project.value =
                Resource.Success(it.copy(members = it.members + ActiveUserDto(it.owner, null)))
            taskView.value = taskView.value.copy(project = it.id)
        }
        project.value.onError {
            val event = SnackBarEvent(it ?: "") { setProject(id) }
            snackBarChannel.send(event)
        }
        assigned.clear()
    }

    fun setTaskTitle(title: String) = viewModelScope.launch {
        taskView.value = taskView.value.copy(title = title)
    }

    fun setTaskDescription(description: String) = viewModelScope.launch {
        taskView.value = taskView.value.copy(description = description)
    }

    fun toggleTaskAssigned(userId: String) = viewModelScope.launch {
        val exist = assigned.remove(userId)
        if (!exist)
            assigned.add(userId)
    }

    fun setTaskEstimatedTime(estimatedTime: Int?) = viewModelScope.launch {
        taskView.value = taskView.value.copy(estimatedTime = estimatedTime)
    }

    fun setTaskMilestoneTitle(value: String) = viewModelScope.launch {
        taskView.value = taskView.value.copy(milestoneTitle = value)
    }

    fun setTaskPriority(priority: Priority) = viewModelScope.launch {
        taskView.value = taskView.value.copy(priority = priority)
    }

    fun setTaskFinishDate(finishDate: Date) = viewModelScope.launch {
        taskView.value = taskView.value.copy(finishDate = finishDate)
    }

    fun addTaskItem(value: TaskItem) = viewModelScope.launch {
        taskItems.add(value)
    }

    fun removeTaskItem(value: TaskItem) = viewModelScope.launch {
        taskItems.remove(value)
    }

    fun toggleIsMileStone(value: Boolean) {
        taskView.value = taskView.value.copy(isMilestone = value)
    }

    fun saveTask() {
        viewModelScope.launch {
            val task = taskView.value.toTask().copy(
                taskItems = taskItems,
                assigned = getAssignedMembers()
            )
            val result = if (isUpdating)
                updateTaskUseCase(task)
            else
                createTaskUseCase(task)
            result.onSuccess {
                val event = SnackBarEvent("task have been saved", null) {}
                snackBarChannel.send(event)
            }

            result.onError {
                val event = SnackBarEvent(it ?: "") { saveTask() }
                snackBarChannel.send(event)
            }
        }
    }

    private fun getAssignedMembers() =
        project.value.data?.members?.filter { assigned.contains(it.user.id) }
            ?.map { it.toActiveUser() } ?: emptyList()

    private fun getInitializedTaskView() = TaskView(
        id = "",
        title = "",
        owner = User("", "", null, null, ""),
        description = null,
        assigned = mutableListOf(),
        status = TaskStatus.Pending,
        estimatedTime = null,
        priority = Priority.Medium,
        taskItems = emptyList(),
        comments = mutableListOf(),
        completeDate = null,
        finishDate = null,
        project = ""
    )

    fun hasPermission(requiredPermission: Permission): Boolean {
        return currentUserTag?.isUserAuthorized(requiredPermission) ?: true
    }

    fun setOwner(user: User) {
        taskView.value = taskView.value.copy(owner = user)
    }

}