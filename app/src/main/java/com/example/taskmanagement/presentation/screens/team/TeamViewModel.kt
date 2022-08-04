package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class TeamViewModel(
    private val repository: IMainRepository,
    private val teamId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    val tagToAssign: MutableState<Tag?> = mutableStateOf(null)
    val showDialogTag = mutableStateOf(false)
    val taggedMembersList = mutableStateListOf<ActiveUser>()

    init {
        getTeam()
    }

    fun getTeam() = viewModelScope.launch {
        team.value = repository.getUserTeam(teamId)
    }

    fun toggleAssignTagDialog(tag: Tag? = null) {
        tagToAssign.value = tag
        showDialogTag.value = !showDialogTag.value
    }

    fun addMember(memberId: String, tagId: String?) = viewModelScope.launch {
        taggedMembersList.removeIf { memberId == it.id }
        taggedMembersList.add(ActiveUser(memberId, tagId))
    }

    fun saveTaggedMembers(){

    }
}