package com.example.taskmanagement.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.useCases.tasks.GetCurrentUserTasksUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCurrentUserTasksUseCase: GetCurrentUserTasksUseCase
) : ViewModel() {
    private val _tasks = MutableStateFlow<Resource<List<Task>>>(Resource.Initialized())
    val tasks: StateFlow<Resource<List<Task>>> = _tasks
    private val _currentTaskStatus = MutableStateFlow(TaskStatus.InProgress)
    val currentTaskStatus = _currentTaskStatus.asStateFlow()
    private val _snackBarChannel = Channel<SnackBarEvent>()
    val snackBarChannel = _snackBarChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getDashboard()
        }
    }

    fun observeTasks(): Flow<Map<TaskStatus, List<Task>>> {
        return channelFlow {
            _tasks.collectLatest { resource ->
                resource.onSuccess { tasks ->
                    val taskGroups = tasks.groupBy { it.status }
                    send(taskGroups)
                }
            }
        }
    }

    private suspend fun getDashboard() {
        _tasks.update { getCurrentUserTasksUseCase(Unit) }
        _tasks.value.onError {
            _snackBarChannel.send(SnackBarEvent(it ?: "") { getDashboard() })
        }
    }

    fun onTaskStatusChanged(taskStatus: TaskStatus) {
        _currentTaskStatus.update { taskStatus }
    }

}