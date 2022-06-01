package com.example.taskmanagement.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.data_models.Task
import com.example.taskmanagement.domain.data_models.TaskDetails
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MainRepository
) : ViewModel() {
    val tasks = mutableStateOf<Resource<List<Task>>>(Resource.Initialized())

    init {
        getUserTasks()
    }

    fun getUserTasks() {
        viewModelScope.launch {
            tasks.value = repository.getUserTasks()
        }
    }


}