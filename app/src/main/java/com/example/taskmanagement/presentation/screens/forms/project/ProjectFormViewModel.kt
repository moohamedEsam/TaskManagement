package com.example.taskmanagement.presentation.screens.forms.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProjectFormViewModel(
    private val repository: MainRepository,
    private val teamId: String,
    private val projectId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val teams = mutableStateOf<Resource<List<Team>>>(Resource.Initialized())
    val isUpdating = projectId.isNotBlank()
    private var currentUserPermission: Resource<Tag> = Resource.Initialized()
    var projectView = mutableStateOf(getInitializedProjectView())
    private val snackbarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackbarChannel.receiveAsFlow()
    val members = mutableStateListOf<User>()
    val showTeamDialog = mutableStateOf(false)
    val showOwnerDialog = mutableStateOf(false)


    init {
        viewModelScope.launch {
            if (teamId.isNotBlank())
                assignTeam(teamId)

            if (isUpdating) {
                getUserTag()
                getProject()
            }
        }
    }

    private suspend fun getProject() {
        val result = repository.getProject(projectId)
        result.onSuccess {
            projectView.value = it
            members.addAll(it.members.map { activeUser -> activeUser.user })
        }
        result.onError {
            val event = SnackBarEvent(it ?: "") {
                getProject()
            }
            snackbarChannel.send(event)
        }

    }

    fun setTeams() {
        viewModelScope.launch {
            teams.value = repository.getUserTeams()
            teams.value.onError {
                val event = SnackBarEvent(it ?: "") { setTeams() }
                snackbarChannel.send(event)
            }
        }
    }

    private fun getUserTag() {
        viewModelScope.launch {
            currentUserPermission = repository.getUserTag("projects", projectId)
            currentUserPermission.onError {
                val event = SnackBarEvent(it ?: "") { getUserTag() }
                snackbarChannel.send(event)
            }
        }
    }

    fun setTeam(id: String) {
        viewModelScope.launch {
            assignTeam(id)
        }
    }

    private suspend fun assignTeam(id: String) {
        team.value = repository.getUserTeam(id)
        team.value.onError {
            val event = SnackBarEvent(it ?: "") { setTeam(id) }
            snackbarChannel.send(event)
        }
        team.value.onSuccess {
            if (isUpdating)
                team.value =
                    Resource.Success(it.copy(members = it.members + ActiveUserDto(it.owner, null)))
            members.clear()
        }
    }

    private fun getInitializedProjectView() = ProjectView(
        name = "",
        owner = User("", "", null, null, ""),
        description = "",
        members = emptyList(),
        tasks = emptyList(),
        team = teamId,
        id = projectId,
        tags = emptyList()
    )

    fun toggleProjectMember(user: User) = viewModelScope.launch {
        val exist = members.remove(user)
        if (!exist)
            members.add(user)
    }

    fun setProjectName(name: String) {
        projectView.value = projectView.value.copy(name = name)
    }

    fun setProjectDescription(description: String) {
        projectView.value = projectView.value.copy(description = description)
    }

    fun setProjectOwner(owner: User) {
        toggleProjectMember(projectView.value.owner)
        projectView.value = projectView.value.copy(owner = owner)
        members.remove(owner)
    }

    fun saveProject() {
        viewModelScope.launch {
            val project =
                projectView.value.toProject()
                    .copy(members = getSelectedMembers())
            val result = if (isUpdating)
                repository.updateProject(project)
            else
                repository.saveProject(project)
            result.onSuccess {
                val event = SnackBarEvent("project has been saved", null) {}
                snackbarChannel.send(event)
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { saveProject() }
                snackbarChannel.send(event)
            }
        }
    }

    private fun getSelectedMembers() =
        team.value.data?.members?.filter { members.contains(it.user) }
            ?.map { it.toActiveUser() } ?: emptyList()

    fun toggleOwnerDialog() {
        showOwnerDialog.value = !showOwnerDialog.value
    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return currentUserPermission.data?.isUserAuthorized(requiredPermission) ?: true
    }

    fun toggleTeamDialog() {
        showTeamDialog.value = !showTeamDialog.value
    }
}