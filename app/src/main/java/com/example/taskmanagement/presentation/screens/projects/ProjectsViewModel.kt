package com.example.taskmanagement.presentation.screens.projects

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class ProjectsViewModel(private val repository: IMainRepository) : ViewModel() {
    val projects = mutableStateOf<Resource<List<Project>>>(Resource.Initialized())

    init {
        getProjects()
    }

    fun getProjects() = viewModelScope.launch {
        projects.value = repository.getUserProjects()
    }
}