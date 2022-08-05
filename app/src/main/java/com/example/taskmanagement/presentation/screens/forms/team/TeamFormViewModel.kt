package com.example.taskmanagement.presentation.screens.forms.team

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.useCases.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.UpdateTeamUseCase
import kotlinx.coroutines.launch
import java.util.*

class TeamFormViewModel(
    private val repository: IMainRepository,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val createTeamUseCase: CreateTeamUseCase,
    private val teamId: String
) : ViewModel() {
    private var teamView: Resource<TeamView> = Resource.Initialized()
    private val currentUser: MutableState<Resource<User>> = mutableStateOf(Resource.Initialized())
    val isUpdating = mutableStateOf(false)
    val team = mutableStateOf(Team("", null, "", emptyList()))
    val members = mutableStateListOf<User>()
    val showDialog = mutableStateOf(false)
    val memberSuggestions = mutableStateOf(emptyList<User>())
    val teamNameValidationResult = mutableStateOf(ValidationResult(true))

    init {
        viewModelScope.launch {
            setCurrentUser()
            getTeam()
        }
    }

    private suspend fun setCurrentUser() {
        currentUser.value = repository.getUserProfile()
    }


    fun toggleDialog() {
        showDialog.value = !showDialog.value
    }


    private suspend fun getTeam() {
        if (teamId.isNotBlank()) {
            teamView = repository.getUserTeam(teamId)
            isUpdating.value = true
        } else {
            teamView =
                if (currentUser.value is Resource.Success && currentUser.value.data != null) {
                    Resource.Success(getInitializedTeam())
                } else
                    Resource.Error("couldn't get current profile")
            isUpdating.value = false
        }
        teamView.onSuccess {
            team.value = it.toTeam()
        }

    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        return when (isUpdating.value) {
            true -> {
                if (teamView.data?.owner?.id == currentUser.value.data?.id)
                    return true
                val teamUser =
                    teamView.data?.members?.find { it.user.id == currentUser.value.data?.id }
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
        owner = currentUser.value.data!!,
        members = emptyList(),
        projects = emptyList(),
        id = UUID.randomUUID().toString(),
        tags = emptyList()
    )

    fun setName(value: String) = viewModelScope.launch {
        team.value = team.value.copy(name = value)
        teamNameValidationResult.value = if (value.isBlank())
            ValidationResult(false, "title can't be blank")
        else
            ValidationResult(true)
    }

    fun setDescription(value: String) = viewModelScope.launch {
        team.value = team.value.copy(description = value)
    }

    fun setOwner(value: String) = viewModelScope.launch {
        team.value = team.value.copy(owner = value)
    }

    fun addMember(value: User) {
        removeMember(value.id)
        members.add(value)
        toggleDialog()
    }


    fun saveTeam(snackbarHostState: SnackbarHostState) = viewModelScope.launch {
        if (!teamNameValidationResult.value.isValid)
            return@launch
        team.value = team.value.copy(members = members.map { ActiveUser(it.id, null) })
        val result = if (isUpdating.value)
            updateTeamUseCase(CreateTeamUseCase.Params(team.value))
        else
            createTeamUseCase(CreateTeamUseCase.Params(team.value))
        if (result is Resource.Success)
            snackbarHostState.showSnackbar("success")
        else
            snackbarHostState.showSnackbar(result.message ?: "")
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