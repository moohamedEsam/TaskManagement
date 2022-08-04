package com.example.taskmanagement.presentation.screens.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: IMainRepository,
    private val taskId: String
) : ViewModel() {
    val task = mutableStateOf<Resource<TaskView>>(Resource.Initialized())

    init {
        getTask()
    }

    fun getTask() = viewModelScope.launch {
        task.value = Resource.Loading()
        task.value = repository.getTask(taskId)
    }
}