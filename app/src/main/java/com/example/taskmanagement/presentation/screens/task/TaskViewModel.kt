package com.example.taskmanagement.presentation.screens.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.data_models.TaskDetails
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: MainRepository,
    private val taskId: String
) : ViewModel() {
    val task = mutableStateOf<Resource<TaskDetails>>(Resource.Initialized())

    init {
        getTask()
    }

    fun getTask() = viewModelScope.launch {
        task.value = Resource.Loading()
        task.value = repository.getTask(taskId)
    }
}