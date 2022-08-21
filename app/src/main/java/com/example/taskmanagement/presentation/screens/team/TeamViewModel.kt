package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TeamViewModel(
    private val repository: MainRepository,
    private val teamId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val taggedMembersList = mutableStateListOf<ActiveUserDto>()
    val updateMade = mutableStateOf(false)
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    private val currentUserTag: MutableState<Tag?> = mutableStateOf(null)
    val invitations = mutableStateOf(emptySet<User>())
    val suggestions = mutableStateOf(emptyList<User>())

    init {
        getTeam()
    }

    private fun getTeam() {
        viewModelScope.launch {
            team.value = repository.getUserTeam(teamId)
            if (team.value is Resource.Error) {
                val event = SnackBarEvent(team.value.message ?: "", "Retry") {
                    getTeam()
                }
                snackBarChannel.send(event)
            }
            team.value.onSuccess {
                taggedMembersList.addAll(it.members)
            }
        }
    }

    private fun getCurrentUserTag() {
        viewModelScope.launch {
            val result = repository.getUserTag("teams", teamId)
            result.onSuccess {
                currentUserTag.value = it
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { getCurrentUserTag() }
                snackBarChannel.send(event)
            }
        }
    }

    fun toggleMemberToTaggedMembers(user: User, tag: Tag) {
        updateMade.value = true
        val index = taggedMembersList.indexOfFirst { user.id == it.user.id }
        if (taggedMembersList[index].tag?.id != tag.id)
            taggedMembersList[index] = ActiveUserDto(user, tag)
        else
            taggedMembersList[index] = ActiveUserDto(user, null)
    }

    fun toggleUserToInvitations(user: User) {
        if (invitations.value.contains(user))
            invitations.value = invitations.value - user
        else
            invitations.value = invitations.value + user
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            val result = repository.searchMembers(query)
            result.onSuccess {
                suggestions.value =
                    it - (team.value.data?.members?.map { activeUser -> activeUser.user }
                        ?: emptyList()).toSet()
            }
            result.onError {
                val event = SnackBarEvent(it ?: "", null) {}
                snackBarChannel.send(event)
            }
        }
    }

    fun sendInvitations() {
        viewModelScope.launch {
            val result = repository.sendInvitations(teamId, invitations.value.map { it.id })
            result.onSuccess {
                val event = SnackBarEvent("invitations has been sent", null) {}
                snackBarChannel.send(event)
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { sendInvitations() }
                snackBarChannel.send(event)
            }
        }
    }

    fun saveTaggedMembers() {
        viewModelScope.launch {
            val result = repository.assignTag(
                teamId,
                ParentRoute.Teams,
                taggedMembersList.map { it.toActiveUser() })
            result.onError {
                val event = SnackBarEvent(it ?: "") {
                    saveTaggedMembers()
                }
                snackBarChannel.send(event)
            }
            result.onSuccess {
                val event = SnackBarEvent("changes has been saved", null) {}
                snackBarChannel.send(event)
            }
        }
    }

    fun isEditIconVisible() =
        currentUserTag.value?.permissions?.any {
            it == Permission.EditName || it == Permission.EditOwner || it == Permission.EditMembers || it == Permission.FullControl
        } ?: true

    fun isShareIconVisible() =
        currentUserTag.value?.permissions?.any {
            it == Permission.Share || it == Permission.FullControl
        } ?: true

}