package com.example.taskmanagement.presentation.screens.taskForm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.TaskItem
import com.example.taskmanagement.domain.dataModels.TaskStatus
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch
import java.util.*

class TaskFormViewModel(private val repository: MainRepository) : ViewModel() {
    val task = mutableStateOf(
        Task(
            title = "",
            owner = "",
            description = "",
            assigned = mutableListOf(),
            project = "",
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

    fun setTaskTitle(title: String) =viewModelScope.launch{
        task.value = task.value.copy(title = title)
    }

    fun setTaskDescription(description: String) =viewModelScope.launch{
        task.value = task.value.copy(description = description)
    }

    fun addTaskAssigned(userId: String) =viewModelScope.launch{
        task.value.assigned?.add(userId)
    }

    fun setTaskStatus(status: TaskStatus) =viewModelScope.launch{
        task.value = task.value.copy(status = status)
    }

    fun setTaskItems(taskItem: TaskItem) =viewModelScope.launch{
        task.value.taskItems.add(taskItem)
    }

    fun setTaskEstimatedTime(estimatedTime: Int?) =viewModelScope.launch{
        task.value = task.value.copy(estimatedTime = estimatedTime)
    }

    fun setTaskPriority(priority: Priority) =viewModelScope.launch{
        task.value = task.value.copy(priority = priority)
    }

    fun setTaskCompleteDate(completeDate: Date) =viewModelScope.launch{
        task.value = task.value.copy(completeDate = completeDate)
    }

    fun setTaskFinishDate(finishDate: Date) =viewModelScope.launch{
        task.value = task.value.copy(finishDate = finishDate)
    }
}