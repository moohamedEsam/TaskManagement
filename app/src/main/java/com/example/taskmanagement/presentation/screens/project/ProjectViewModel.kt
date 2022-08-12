package com.example.taskmanagement.presentation.screens.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: MainRepository,
    private val projectId: String
) : ViewModel() {
    val project = mutableStateOf<Resource<ProjectView>>(Resource.Initialized())


    init {
        getProject()
    }
    fun getProject() = viewModelScope.launch {
        project.value = repository.getProject(projectId)
    }

}
