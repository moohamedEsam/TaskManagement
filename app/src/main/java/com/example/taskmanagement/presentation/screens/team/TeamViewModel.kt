package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Project
import com.example.taskmanagement.domain.dataModels.Team
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class TeamViewModel(
    private val repository: MainRepository,
    private val teamId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val project = mutableStateOf(
        Project(
            id = "",
            name = "",
            description = "",
            members = emptyList(),
            team = team.value.data?.publicId ?: "",
        )
    )
    val members = mutableStateMapOf<String, Boolean>()

    init {
        getTeam()
    }

    fun getTeam() = viewModelScope.launch {
        team.value = repository.getUserTeam(teamId)
    }

    fun addProjectMember(id: String) = viewModelScope.launch {
        members[id] = members.getOrDefault(id, false).not()
    }

    fun setProjectName(name: String) {
        project.value = project.value.copy(name = name)
    }

    fun setProjectDescription(description: String) {
        project.value = project.value.copy(description = description)
    }

    fun setProjectMembers() {
        project.value = project.value.copy(members = members.filter { it.value }.keys.toList())
    }

    fun saveProject() = viewModelScope.launch {
        project.value = project.value.copy(
            team = team.value.data?.publicId ?: "",
            members = members.filter { it.value }.keys.toList()
        )
        repository.saveProject(project.value)
    }

    fun resetProject() = viewModelScope.launch{
        project.value = project.value.copy(
            name = "",
            description = "",
            members = emptyList()
        )
    }
}