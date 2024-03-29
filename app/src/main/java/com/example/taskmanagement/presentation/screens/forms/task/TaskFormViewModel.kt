package com.example.taskmanagement.presentation.screens.forms.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.projects.GetCurrentUserProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.tasks.CreateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import com.example.taskmanagement.domain.validatorsImpl.TaskFormValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class TaskFormViewModel(
    private val getTaskUseCase: GetTaskUseCase,
    private val getCurrentUserTag: GetCurrentUserTag,
    private val getCurrentUserProjectUseCase: GetCurrentUserProjectUseCase,
    private val getProjectUseCase: GetProjectUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val validator: TaskFormValidator,
    projectId: String,
    private val taskId: String
) : ViewModel() {
    private val _project = MutableStateFlow<Resource<ProjectView>>(Resource.Initialized())
    val project = _project.asStateFlow()
    private val _projects = MutableStateFlow<Resource<List<Project>>>(Resource.Initialized())
    val projects = _projects.asStateFlow()
    val isUpdating = taskId.isNotBlank()
    private var currentUserTag: Tag? = null
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    private val _assigned = MutableStateFlow(emptySet<String>())
    val assigned = _assigned.asStateFlow()
    private val _taskView = MutableStateFlow(getInitializedTaskView())
    val taskView = _taskView.asStateFlow()
    private val _taskItems = MutableStateFlow(emptySet<TaskItem>())
    val taskItems = _taskItems.asStateFlow()

    private val _taskEstimatedTimeText = MutableStateFlow("")
    val taskEstimatedTimeText = _taskEstimatedTimeText.asStateFlow()

    private val _taskTitleValidationResult = MutableStateFlow(ValidationResult(true))
    val taskTitleValidationResult = _taskTitleValidationResult.asStateFlow()

    private val _taskMilestoneTitleValidationResult = MutableStateFlow(ValidationResult(true))
    val taskMilestoneTitleValidationResult = _taskMilestoneTitleValidationResult.asStateFlow()

    private val _taskEstimatedTimeValidationResult = MutableStateFlow(ValidationResult(true))
    val taskEstimatedTimeValidationResult = _taskEstimatedTimeValidationResult.asStateFlow()

    private val _taskFinishDateValidationResult = MutableStateFlow(ValidationResult(true))
    val taskFinishDateValidationResult = _taskFinishDateValidationResult.asStateFlow()

    private val _taskItemValidationResult = MutableStateFlow(ValidationResult(true))
    val taskItemValidationResult = _taskItemValidationResult.asStateFlow()
    val canSave = combine(
        _taskTitleValidationResult,
        _project,
        _taskItems
    ) { titleValidation, project, taskItems ->
        titleValidation.isValid && project is Resource.Success && taskItems.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

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
            _taskView.update { _ -> it }
            _assigned.update { _ -> it.assigned.map { user -> user.id }.toSet() }
            _taskItems.update { _ -> it.taskItems.toSet() }
        }

        result.onError {
            val event = SnackBarEvent(it ?: "") { setTask(taskId) }
            snackBarChannel.send(event)
        }
    }


    fun getProjects() {
        viewModelScope.launch {
            if (_project.value is Resource.Loading) return@launch
            _projects.update { Resource.Loading() }
            _projects.update {
                getCurrentUserProjectUseCase(Unit)
            }
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
        _project.update {
            val result = getProjectUseCase(id)
            var resultProjectView = result.data
            result.onSuccess { projectView ->
                resultProjectView =
                    projectView.copy(members = projectView.members + ActiveUserDto(projectView.owner))
            }
            result.copy(resultProjectView)
        }

        project.value.onError {
            val event = SnackBarEvent(it ?: "") { setProject(id) }
            snackBarChannel.send(event)
        }

        project.value.onSuccess {
            _assigned.update { emptySet() }
            _taskView.update { taskView -> taskView.copy(project = it.id) }
        }

    }

    fun setTaskTitle(title: String) = viewModelScope.launch {
        _taskView.update { it.copy(title = title) }
        _taskTitleValidationResult.update { validator.nameValidator.validate(title) }
    }

    fun setTaskDescription(description: String) = viewModelScope.launch {
        _taskView.update { it.copy(description = description) }
    }

    fun toggleTaskAssigned(userId: String) = viewModelScope.launch {
        _assigned.update {
            if (it.contains(userId))
                it - userId
            else
                it + userId
        }
    }

    fun setTaskEstimatedTime(estimatedTime: String) = viewModelScope.launch {
        _taskEstimatedTimeText.update { estimatedTime }
        _taskEstimatedTimeValidationResult.update {
            validator.estimatedTimeValidator.validate(
                estimatedTime
            )
        }
        if (taskEstimatedTimeValidationResult.value.isValid)
            _taskView.update { it.copy(estimatedTime = estimatedTime.toInt()) }
    }

    fun setTaskMilestoneTitle(milestoneTitle: String) = viewModelScope.launch {
        _taskView.update { it.copy(milestoneTitle = milestoneTitle) }
        _taskMilestoneTitleValidationResult.update { validator.nameValidator.validate(milestoneTitle) }
    }

    fun setTaskPriority(priority: Priority) = viewModelScope.launch {
        _taskView.update { it.copy(priority = priority) }
    }

    fun setTaskFinishDate(finishDate: Date) = viewModelScope.launch {
        _taskView.update { it.copy(finishDate = finishDate) }
        _taskFinishDateValidationResult.update { validator.dateValidator.validate(finishDate) }
    }

    fun validateTaskItemTitle(title: String) = viewModelScope.launch {
        _taskItemValidationResult.update { validator.nameValidator.validate(title) }
    }

    fun addTaskItem(value: TaskItem) = viewModelScope.launch {
        _taskItemValidationResult.update { validator.nameValidator.validate(value.title) }
        if (taskItemValidationResult.value.isValid)
            _taskItems.update { it + value }
    }

    fun removeTaskItem(value: TaskItem) = viewModelScope.launch {
        _taskItems.update { it - value }
    }

    fun toggleIsMileStone(value: Boolean) {
        _taskView.update { it.copy(isMilestone = value) }
    }

    fun saveTask(onLoading: () -> Unit, onDone: () -> Unit): Job = viewModelScope.launch {
        if (!validator.isFormValid(
                taskTitleValidationResult.value,
                taskEstimatedTimeValidationResult.value,
                taskFinishDateValidationResult.value,
                taskMilestoneTitleValidationResult.value
            )
        ) return@launch
        onLoading()
        val task = taskView.value.toTask().copy(
            taskItems = taskItems.value.toList(),
            assigned = getAssignedMembers()
        )
        val result = if (isUpdating)
            updateTaskUseCase(task)
        else
            createTaskUseCase(task)
        onDone()
        result.onSuccess {
            val event = SnackBarEvent("task have been saved", null) {}
            snackBarChannel.send(event)
        }

        result.onError {
            val event = SnackBarEvent(it ?: "") { saveTask(onLoading, onDone) }
            snackBarChannel.send(event)
        }

    }

    private fun getAssignedMembers() =
        project.value.data?.members?.filter { assigned.value.contains(it.user.id) }
            ?.map { it.user.id } ?: emptyList()

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

    fun hasPermission(vararg requiredPermission: Permission): Boolean {
        return currentUserTag?.isUserAuthorized(requiredPermission = requiredPermission) ?: true
    }

    fun setOwner(user: User) {
        _taskView.update { it.copy(owner = user) }
    }

}