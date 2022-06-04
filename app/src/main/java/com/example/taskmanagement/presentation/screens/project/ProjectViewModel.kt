package com.example.taskmanagement.presentation.screens.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.TaskItem
import com.example.taskmanagement.domain.dataModels.TaskStatus
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel(
    private val repository: MainRepository,
    private val projectId: String
) : ViewModel() {
    val project = mutableStateOf<Resource<ProjectView>>(Resource.Initialized())
    val task = mutableStateOf(
        Task(
            title = "",
            owner = "",
            description = "",
            assigned = mutableListOf(),
            project = project.value.data?.publicId ?: "",
            status = TaskStatus.Pending,
            taskItems = mutableListOf(),
            comments = mutableListOf(),
            estimatedTime = null,
            priority = Priority.Low,
            completeDate = null,
            finishDate = null,
            id = ""
        )
    )
    val taskItems = mutableStateListOf<TaskItem>()
    val assigned = mutableStateMapOf<String, Boolean>()

    init {
        getProject()
    }

    fun setTaskTitle(title: String) = viewModelScope.launch {
        task.value = task.value.copy(title = title)
    }

    fun setTaskDescription(description: String) = viewModelScope.launch {
        task.value = task.value.copy(description = description)
    }

    fun addTaskAssigned(userId: String) = viewModelScope.launch {
        assigned[userId] = assigned.getOrDefault(userId, false).not()
    }

    fun setTaskEstimatedTime(estimatedTime: Int?) = viewModelScope.launch {
        task.value = task.value.copy(estimatedTime = estimatedTime)
    }

    fun setTaskPriority(priority: Priority) = viewModelScope.launch {
        task.value = task.value.copy(priority = priority)
    }

    fun setTaskFinishDate(finishDate: Date) = viewModelScope.launch {
        task.value = task.value.copy(finishDate = finishDate)
    }

    fun getProject() = viewModelScope.launch {
        project.value = repository.getProject(projectId)
    }

    fun addTaskItem(value: String) = viewModelScope.launch {
        taskItems.add(TaskItem(value, false))
    }

    fun resetTask() = viewModelScope.launch {
        task.value = Task(
            title = "",
            owner = "",
            description = "",
            assigned = mutableListOf(),
            project = project.value.data?.publicId ?: "",
            status = TaskStatus.Pending,
            taskItems = mutableListOf(),
            comments = mutableListOf(),
            estimatedTime = null,
            priority = Priority.Low,
            completeDate = null,
            finishDate = null,
            id = ""
        )
        taskItems.clear()
        assigned.clear()
    }

    fun saveTask() = viewModelScope.launch {
        task.value = task.value.copy(
            assigned = assigned.filter { it.value }.keys.toMutableList(),
            taskItems = taskItems.toMutableList(),
            project = project.value.data?.publicId ?: ""
        )
        println(task.value)
        repository.saveTask(task.value)
    }
}
