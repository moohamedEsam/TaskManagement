package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.teams.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.UpdateTeamUseCase
import com.example.taskmanagement.domain.useCases.user.SearchMembersUseCase
import com.example.taskmanagement.presentation.utils.MemberManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TeamFormViewModel(
    private val searchMembersUseCase: SearchMembersUseCase,
    private val getCurrentUserTag: GetCurrentUserTag,
    private val getTeamUseCase: GetTeamUseCase,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val memberManager: MemberManager<TeamView>,
    private val teamId: String
) : ViewModel() {
    var teamView = mutableStateOf(getInitializedTeam())
    val members = mutableStateListOf<String>()

    private var currentUserTag: Resource<Tag> = Resource.Initialized()
    val isUpdating = teamId.isNotBlank()

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
            currentUserTag = getCurrentUserTag(GetCurrentUserTag.Params(ParentRoute.Teams, teamId))
            currentUserTag.onError {
                val event = SnackBarEvent(it ?: "") { setCurrentUserTag() }
                snackBarChannel.send(event)
            }
        }
    }


    private fun setTeamView() {
        viewModelScope.launch {
            val result = getTeamUseCase(teamId)
            if (result is Resource.Error) {
                val event = SnackBarEvent(result.message ?: "") { setTeamView() }
                snackBarChannel.send(event)
            }
            result.onSuccess {
                teamView.value = it
                members.clear()
                members.addAll(it.members.map { user -> user.user.id })
            }
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
        tags = emptyList(),
        pendingMembers = emptyList()
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
        addMember(teamView.value.owner)
        members.add(teamView.value.owner.id)
        teamView.value = teamView.value.copy(owner = value)
        members.remove(value.id)
    }

    fun toggleMember(value: User) {
        viewModelScope.launch {
            memberManager.toggleMember(value, members, teamView)
        }
    }


    fun addMember(value: User) {
        viewModelScope.launch {
            memberManager.addMember(value, members, teamView)
        }
    }


    fun saveTeam(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            if (!teamNameValidationResult.value.isValid)
                return@launch
            val team = teamView.value.toTeam()
            if (isUpdating)
                team.members = team.members.filter { members.contains(it.id) }

            val result = if (isUpdating)
                updateTeamUseCase(CreateTeamUseCase.Params(team))
            else
                createTeamUseCase(CreateTeamUseCase.Params(team))
            val event = if (result is Resource.Success) {
                onSuccess(result.data?.id ?: "")
                SnackBarEvent("team has been saved", null) {}
            } else
                SnackBarEvent(result.message ?: "") { saveTeam(onSuccess) }
            snackBarChannel.send(event)
        }
    }


    fun searchMembers(query: String) = viewModelScope.launch {
        val response = searchMembersUseCase(query)
        response.onSuccess {
            memberSuggestions.value = it
        }
    }
}