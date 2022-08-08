package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.UpdateTeamUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TeamFormViewModel(
    private val repository: MainRepository,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val teamId: String
) : ViewModel() {
    var teamView = mutableStateOf(getInitializedTeam())
    val members = mutableStateListOf<User>()
    private var currentUserTag: Resource<Tag> = Resource.Initialized()
    val isUpdating = mutableStateOf(false)
    val membersDialog = mutableStateOf(false)
    val ownerDialog = mutableStateOf(false)

    val memberSuggestions = mutableStateOf(emptyList<User>())
    val teamNameValidationResult = mutableStateOf(ValidationResult(true))
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        if (teamId.isNotBlank()) {
            setCurrentUserTag()
            setTeamView()
        }
    }

    private fun setCurrentUserTag() {
        viewModelScope.launch {
            currentUserTag = repository.getUserTag("teams", teamId)
            currentUserTag.onError {
                val event = SnackBarEvent(it ?: "") { setCurrentUserTag() }
                snackBarChannel.send(event)
            }
        }
    }


    fun toggleMembersDialog() {
        membersDialog.value = !membersDialog.value
    }

    fun toggleOwnerDialog() {
        ownerDialog.value = !ownerDialog.value
    }


    private fun setTeamView() {
        viewModelScope.launch {
            val result = repository.getUserTeam(teamId)
            if (result is Resource.Error) {
                val event = SnackBarEvent(result.message ?: "") { setTeamView() }
                snackBarChannel.send(event)
            }
            result.onSuccess {
                teamView.value = it
                members.clear()
                members.addAll(it.members.map { activeUser -> activeUser.user })
            }
            isUpdating.value = true
        }
    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return currentUserTag.data?.isUserAuthorized(requiredPermission) ?: true
    }

    private fun getInitializedTeam() = TeamView(
        name = "",
        description = null,
        owner = User("", "", null, null, ""),
        members = emptyList(),
        projects = emptyList(),
        id = UUID.randomUUID().toString(),
        tags = emptyList()
    )

    fun setName(value: String) = viewModelScope.launch {
        teamView.value = teamView.value.copy(name = value)
        teamNameValidationResult.value = if (value.isBlank())
            ValidationResult(false, "title can't be blank")
        else
            ValidationResult(true)
    }

    fun setDescription(value: String) = viewModelScope.launch {
        teamView.value = teamView.value.copy(description = value)
    }

    fun setOwner(value: User) = viewModelScope.launch {
        members.remove(value)
        members.add(teamView.value.owner)
        teamView.value = teamView.value.copy(owner = value)
    }

    fun toggleMember(value: User) {
        val exist = members.remove(value)
        if (!exist)
            members.add(value)
    }


    fun saveTeam() {
        viewModelScope.launch {
            if (!teamNameValidationResult.value.isValid)
                return@launch
            val team =
                teamView.value.toTeam().copy(members = members.map { ActiveUser(it.id, null) })
            val result = if (isUpdating.value)
                updateTeamUseCase(CreateTeamUseCase.Params(team))
            else
                createTeamUseCase(CreateTeamUseCase.Params(team))
            val event = if (result is Resource.Success)
                SnackBarEvent("team has been saved", null) {}
            else
                SnackBarEvent(result.message ?: "") { saveTeam() }
            snackBarChannel.send(event)
        }
    }

    fun searchMembers(query: String) = viewModelScope.launch {
        val response = repository.searchMembers(query)
        response.onSuccess {
            memberSuggestions.value = it
        }
    }
}