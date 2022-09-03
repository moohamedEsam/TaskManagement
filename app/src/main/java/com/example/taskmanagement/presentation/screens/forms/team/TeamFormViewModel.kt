package com.example.taskmanagement.presentation.screens.forms.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.teams.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.UpdateTeamUseCase
import com.example.taskmanagement.domain.useCases.user.SearchMembersUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamFormViewModel(
    private val searchMembersUseCase: SearchMembersUseCase,
    private val getCurrentUserTag: GetCurrentUserTag,
    private val getTeamUseCase: GetTeamUseCase,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val validator: BaseValidator,
    private val teamId: String
) : ViewModel() {
    private val _teamView = MutableStateFlow(getInitializedTeam())
    val teamView = _teamView.asStateFlow()
    private val _members = MutableStateFlow(emptySet<User>())
    val members = _members.asStateFlow()

    private var currentUserTag: Resource<Tag> = Resource.Initialized()
    val isUpdating = teamId.isNotBlank()

    private val _membersSuggestions = MutableStateFlow(emptySet<User>())
    val membersSuggestions = _membersSuggestions.asStateFlow()
    private val _teamNameValidationResult = MutableStateFlow(ValidationResult(true))
    val teamNameValidationResult = _teamNameValidationResult.asStateFlow()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    fun setCurrentUserTag(): Job = viewModelScope.launch {
        currentUserTag = getCurrentUserTag(GetCurrentUserTag.Params(ParentRoute.Teams, teamId))
        currentUserTag.onError {
            val event = SnackBarEvent(it ?: "") { setCurrentUserTag() }
            snackBarChannel.send(event)
        }
    }


    fun setTeamView(): Job = viewModelScope.launch {
        val result = getTeamUseCase(teamId)
        if (result is Resource.Error) {
            val event = SnackBarEvent(result.message ?: "") { setTeamView() }
            snackBarChannel.send(event)
        }
        result.onSuccess {
            _teamView.update { _ -> it }
            _members.update { _ -> it.members.map { activeUser -> activeUser.user }.toSet() }
        }

    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return currentUserTag.data?.isUserAuthorized(requiredPermission) ?: true
    }

    private fun getInitializedTeam() = TeamView(owner = User(id = "owner"))

    fun setName(value: String) = viewModelScope.launch {
        _teamView.update { it.copy(name = value) }
        _teamNameValidationResult.update { validator.nameValidator.validate(value) }
    }

    fun setDescription(value: String) = viewModelScope.launch {
        _teamView.update { it.copy(description = value) }
    }

    fun setOwner(value: User) = viewModelScope.launch {
        _members.update {
            it + teamView.value.owner - value
        }
        _teamView.update { it.copy(owner = value) }
    }

    fun toggleMember(value: User) = viewModelScope.launch {
        _members.update {
            if (it.contains(value))
                it - value
            else
                it + value
        }
    }


    fun addMember(value: User) = viewModelScope.launch {
        if (isUpdating)
            return@launch
        _members.update { it + value }
    }


    fun saveTeam(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            if (!teamNameValidationResult.value.isValid)
                return@launch
            val team = teamView.value.toTeam()
            if (isUpdating)
                team.members = _members.value.map { ActiveUser(it.id) }

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
        if (query.isBlank() || query.length < 3)
            return@launch
        val response = searchMembersUseCase(query)
        response.onSuccess {
            _membersSuggestions.update { _ -> it.toSet() }
        }
    }
}