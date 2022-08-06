package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TeamViewModel(
    private val repository: IMainRepository,
    private val teamId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val showTagDialog = mutableStateOf(false)
    val taggedMembersList = mutableStateListOf<ActiveUserDto>()
    val multiSelectMode = mutableStateOf(false)
    val selectedMembers = mutableStateListOf<User>()
    val membersTagsChanged = mutableStateOf(false)
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        getTeam()
    }

    fun getTeam() {
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

    fun toggleTagDialog() {
        showTagDialog.value = !showTagDialog.value
    }

    fun assignTagToSelectedMembers(tag: Tag) = viewModelScope.launch {
        taggedMembersList.removeIf { selectedMembers.contains(it.user) }
        taggedMembersList.addAll(selectedMembers.map { ActiveUserDto(it, tag) })
        membersTagsChanged.value = true
    }

    fun toggleSelectedMember(user: User) = viewModelScope.launch {
        val exist = selectedMembers.remove(user)
        if (!exist)
            selectedMembers.add(user)
    }

    fun toggleMultiSelect() {
        multiSelectMode.value = !multiSelectMode.value
        if (!multiSelectMode.value)
            selectedMembers.clear()
    }

    fun saveTaggedMembers() {
        viewModelScope.launch {
            val result = repository.assignTag(teamId, taggedMembersList.map { it.toActiveUser() })
            if (result is Resource.Error) {
                val event = SnackBarEvent(result.message ?: "") {
                    saveTaggedMembers()
                }
                snackBarChannel.send(event)
            }
        }
    }
}