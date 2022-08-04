package com.example.taskmanagement.presentation.screens.forms.project

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class ProjectFormViewModel(
    private val repository: IMainRepository,
    teamId: String,
    projectId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val teams = mutableStateOf<Resource<List<Team>>>(Resource.Initialized())
    val isUpdating = mutableStateOf(false)
    private val currentUser: MutableState<Resource<User>> = mutableStateOf(Resource.Initialized())
    var projectView: MutableState<Resource<ProjectView>> = mutableStateOf(Resource.Initialized())
    val project = mutableStateOf(
        Project(
            name = "",
            description = "",
            members = emptyList(),
            team = team.value.data?.id ?: "",
        )
    )
    val members: SnapshotStateMap<String, Pair<ActiveUser, Boolean>> = mutableStateMapOf()
    val showTeamDialog = mutableStateOf(false)
    val showTeamMembersDialog = mutableStateOf(false)
    val showOwnerDialog = mutableStateOf(false)
    val userToUpdate: MutableState<ActiveUser?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            if (projectId.isNotBlank()) {
                projectView.value = repository.getProject(projectId)
                isUpdating.value = true
            } else {
                if (teamId.isNotBlank())
                    setTeam(teamId)
                else
                    setTeams()
            }
            projectView.value.onSuccess {
                project.value =
                    Project(it.name, it.owner.id, it.description, it.members, it.team, it.id)
                it.members.forEach { activeUser ->

                }
            }

        }
    }

    private fun setTeams() = viewModelScope.launch {
        teams.value = repository.getUserTeams()
    }

    fun setTeam(id: String) = viewModelScope.launch {
        team.value = repository.getUserTeam(id)
        members.clear()
        team.value.onSuccess {
            it.members.forEach { activeUser ->


            }
        }
    }

    fun getProjectOwner(): String {
        if (!isUpdating.value) return ""
        projectView.value.onSuccess {
        }
        return ""
    }

    fun addProjectMember(activeUser: ActiveUser) = viewModelScope.launch {

        toggleTeamMemberDialog()
    }

    fun setProjectName(name: String) {
        project.value = project.value.copy(name = name)
    }

    fun setProjectDescription(description: String) {
        project.value = project.value.copy(description = description)
    }

    fun setProjectOwner(owner: String) {
        projectView.value.onSuccess {
            val user = if (owner == it.owner.username)
                it.owner.id
            else


            project.value = project.value
        }
    }

    fun saveProject(snackbarHostState: SnackbarHostState) = viewModelScope.launch {
        project.value = project.value.copy(
            team = team.value.data?.id ?: "",
            members = members.filter { it.value.second }.map { (_, value) -> value.first }
        )
        val result = repository.saveProject(project.value)
        if (result is Resource.Success) {
            snackbarHostState.showSnackbar("Project saved")
            resetProject()
        } else if (result is Resource.Error)
            snackbarHostState.showSnackbar(result.message ?: "Error")
    }

    fun resetProject() = viewModelScope.launch {
        project.value = project.value.copy(
            name = "",
            description = "",
            members = emptyList()
        )
    }

    fun toggleOwnerDialog() {
        showOwnerDialog.value = !showOwnerDialog.value
    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return true
    }

    fun toggleTeamDialog() {
        showTeamDialog.value = !showTeamDialog.value
    }

    fun toggleTeamMemberDialog(user: ActiveUser? = null) {
        userToUpdate.value = user
        showTeamMembersDialog.value = !showTeamMembersDialog.value
    }

}