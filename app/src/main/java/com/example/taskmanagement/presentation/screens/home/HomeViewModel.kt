package com.example.taskmanagement.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.taskmanagement.domain.data_models.TaskDetails
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository

class HomeViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    val tasks = mutableStateOf<Resource<List<TaskDetails>>>(Resource.Initialized())

    init {

    }


}