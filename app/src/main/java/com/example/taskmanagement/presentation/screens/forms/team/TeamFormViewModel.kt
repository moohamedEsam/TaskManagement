package com.example.taskmanagement.presentation.screens.forms.team

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Permission
import com.example.taskmanagement.domain.dataModels.Role
import com.example.taskmanagement.domain.dataModels.Team
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.utils.CreateTeamBody
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.TeamUser
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.utils.mappers.toCreateTeamBody
import kotlinx.coroutines.launch
import java.util.*

class TeamFormViewModel(
    private val repository: IMainRepository,
    private val teamId: String
) : ViewModel() {
    private var team: Resource<TeamView> = Resource.Initialized()
    val currentUser: MutableState<Resource<User>> = mutableStateOf(Resource.Initialized())
    val isUpdating = mutableStateOf(false)
    val requestBody = mutableStateOf(CreateTeamBody("", null, "", emptyList()))
    val members = mutableStateMapOf<String, List<Permission>>()
    val showDialog = mutableStateOf(false)
    val usernameValidationResult = mutableStateOf(ValidationResult(true))
    val showDialogWithUser: MutableState<TeamUser?> = mutableStateOf(null)

    init {
        getTeam()
        viewModelScope.launch {
            currentUser.value = repository.getUserProfile()
        }
    }

    fun toggleDialog(user: TeamUser? = null) {
        showDialogWithUser.value = user
        showDialog.value = !showDialog.value
    }


    fun getTeam() {
        viewModelScope.launch {
            if (teamId.isNotBlank()) {
                team = repository.getUserTeam(teamId)
                isUpdating.value = true
            } else {
                team =
                    if (currentUser.value is Resource.Success && currentUser.value.data != null) {
                        Resource.Success(getInitializedTeam())
                    } else
                        Resource.Error("couldn't get current profile")
                isUpdating.value = false
            }
            team.onSuccess {
                it.members.forEach { user ->
                    members[user.user.username] = user.role.permissions
                }
                requestBody.value = it.toCreateTeamBody()
            }
        }
    }

    fun hasPermission(requiredPermission: Permission): Boolean {
        Log.i("TeamFormViewModel", "hasPermission: called")
        return when (isUpdating.value) {
            true -> {
                if (team.data?.owner?.id == currentUser.value.data?.id)
                    return true
                val teamUser = team.data?.members?.find { it.user.id == currentUser.value.data?.id }
                    ?: return false
                teamUser.role.permissions.any { it == requiredPermission || it == Permission.FullControl }
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
        id = UUID.randomUUID().toString()
    )

    fun getCurrentUserRole(): Role {
        return if (isUpdating.value)
            return if ((team.data?.owner ?: "") == currentUser.value.data?.id)
                Role(listOf(Permission.FullControl))
            else
                team.data?.members?.find {
                    it.user.id == (currentUser.value.data?.id ?: "")
                }?.role
                    ?: Role(emptyList())
        else
            Role(listOf(Permission.FullControl))
    }

    fun setName(value: String) = viewModelScope.launch {
        requestBody.value = requestBody.value.copy(name = value)
    }

    fun setDescription(value: String) = viewModelScope.launch {
        requestBody.value = requestBody.value.copy(description = value)
    }

    fun setOwner(value: String) = viewModelScope.launch {
        requestBody.value = requestBody.value.copy(owner = value)
    }

    fun addMember(value: TeamUser) {
        removeMember()
        members[value.username] = value.role.permissions
        toggleDialog()
    }

    fun saveTeam(snackbarHostState: SnackbarHostState) = viewModelScope.launch {
        val teamUsers = buildList {
            members.forEach { (username, permission) ->
                add(TeamUser(username, Role(permission)))
            }
        }
        requestBody.value = requestBody.value.copy(members = teamUsers)
        val result = if (isUpdating.value)
            repository.updateTeam(requestBody.value)
        else
            repository.createTeam(requestBody.value)
        if (result is Resource.Success)
            snackbarHostState.showSnackbar("success")
        else
            snackbarHostState.showSnackbar(result.message ?: "")
    }

    fun removeMember() {
        if (showDialogWithUser.value != null)
            members.remove(showDialogWithUser.value!!.username)
    }
}