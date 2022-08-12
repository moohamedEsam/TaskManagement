package com.example.taskmanagement.presentation.screens.projects

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProjectsViewModel(private val repository: MainRepository) : ViewModel() {
    private val projects = mutableStateOf<Resource<List<Project>>>(Resource.Initialized())
    val filteredProjects = mutableStateOf(emptyList<Project>())
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        getProjects()
    }

    private fun getProjects() {
        viewModelScope.launch {
            projects.value = repository.getUserProjects()
            projects.value.onSuccess {
                filteredProjects.value = it
            }
            projects.value.onError {
                val event = SnackBarEvent(it ?: "") { getProjects() }
                snackBarChannel.send(event)
            }
        }
    }

    fun filter(query: String) {
        projects.value.onSuccess { projectList ->
            filteredProjects.value =
                projectList.filter { it.name.contains(query) || it.description.contains(query) }
        }
    }


}