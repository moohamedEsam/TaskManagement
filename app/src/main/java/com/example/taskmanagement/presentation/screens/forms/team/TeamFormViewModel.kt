package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.useCases.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.UpdateTeamUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TeamFormViewModel(
    private val repository: IMainRepository,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val teamId: String
) : ViewModel() {
    var teamView = mutableStateOf(getInitializedTeam())
    val members = mutableStateListOf<User>()
    private val currentUser: MutableState<Resource<User>> = mutableStateOf(Resource.Initialized())
    val isUpdating = mutableStateOf(false)
    val membersDialog = mutableStateOf(false)
    val ownerDialog = mutableStateOf(false)

    val memberSuggestions = mutableStateOf(emptyList<User>())
    val teamNameValidationResult = mutableStateOf(ValidationResult(true))
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            setCurrentUser()
            setTeam()
        }
    }

    private suspend fun setCurrentUser() {
        currentUser.value = repository.getUserProfile()
        currentUser.value.onSuccess {
            teamView.value = teamView.value.copy(owner = it)
        }
        if (currentUser.value is Resource.Error) {
            val event = SnackBarEvent(currentUser.value.message ?: "") {
                setCurrentUser()
            }
            snackBarChannel.send(event)
        }
    }


    fun toggleMembersDialog() {
        membersDialog.value = !membersDialog.value
    }

    fun toggleOwnerDialog() {
        ownerDialog.value = !ownerDialog.value
    }


    private suspend fun setTeam() {
        if (teamId.isNotBlank())
            getTeamView()

    }

    private suspend fun getTeamView() {
        val result = repository.getUserTeam(teamId)
        if (result is Resource.Error) {
            val event = SnackBarEvent(result.message ?: "") { setTeam() }
            snackBarChannel.send(event)
        }
        result.onSuccess {
            teamView.value = it
            members.clear()
            members.addAll(it.members.map { activeUser -> activeUser.user })
        }
        isUpdating.value = true
    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return when (isUpdating.value) {
            true -> {
                if (teamView.value.owner.id == currentUser.value.data?.id)
                    return true
                val teamUser =
                    teamView.value.members.find { it.user.id == currentUser.value.data?.id }
                        ?: return false
                teamUser.tag?.permissions?.any { it == requiredPermission || it == Permission.FullControl }
                    ?: false
            }
            else -> true
        }
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
        teamView.value = teamView.value.copy(owner = value)
    }

    fun addMember(value: User) {
        removeMember(value.id)
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
                SnackBarEvent("team has been created", null) {}
            else
                SnackBarEvent(result.message ?: "") { saveTeam() }
            snackBarChannel.send(event)
        }
    }

    fun removeMember(userId: String) {
        members.removeIf { activeUser -> activeUser.id == userId }
    }

    fun searchMembers(query: String) = viewModelScope.launch {
        val response = repository.searchMembers(query)
        response.onSuccess {
            memberSuggestions.value = it
        }
    }
}