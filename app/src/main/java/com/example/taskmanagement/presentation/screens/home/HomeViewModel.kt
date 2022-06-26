package com.example.taskmanagement.presentation.screens.home

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.TaskStatus
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: IMainRepository
) : ViewModel() {
    val tasks = mutableStateOf<Resource<List<Task>>>(Resource.Initialized())
    val filteredTasks = mutableStateOf<List<Task>>(emptyList())
    val searchMode = mutableStateOf(false)
    val priorityFilterStates = mutableStateMapOf<Priority, Boolean>()
    val statusFilterStates = mutableStateMapOf<TaskStatus, Boolean>()
    val searchQuery = mutableStateOf("")

    init {
        getUserTasks()
        priorityFilterStates.putAll(Priority.values().associateWith { false })
        statusFilterStates.putAll(TaskStatus.values().associateWith { false })
    }

    fun setFilterState(priority: Priority, state: Boolean) = viewModelScope.launch {
        searchMode.value = true
        priorityFilterStates[priority] = state
        filterTasks()
    }

    fun setFilterState(status: TaskStatus, state: Boolean) = viewModelScope.launch {
        searchMode.value = true
        statusFilterStates[status] = state
        filterTasks()
    }

    fun setSearchQuery(query: String) = viewModelScope.launch {
        searchMode.value = true
        searchQuery.value = query
        filterTasks()
    }

    private fun filterTasks() = viewModelScope.launch {
        filteredTasks.value = tasks.value.data?.filter {
            priorityFilterStates[it.priority] ?: false || statusFilterStates[it.status] ?: false
        } ?: emptyList()
        if (searchQuery.value.isNotBlank()) {
            filteredTasks.value = filteredTasks.value.filter {
                it.title.contains(searchQuery.value, true)
            }
        }
    }

    fun clearFilters() {
        searchMode.value = false
        searchQuery.value = ""
        priorityFilterStates.putAll(Priority.values().associateWith { false })
        statusFilterStates.putAll(TaskStatus.values().associateWith { false })
    }

    fun getUserTasks() {
        viewModelScope.launch {
            tasks.value = repository.getUserTasks()
        }
    }


}