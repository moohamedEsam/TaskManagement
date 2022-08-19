package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.utils.MemberManager

class TeamMemberManagerCreateCase : MemberManager<TeamView> {
    override suspend fun toggleMember(
        value: User,
        members: SnapshotStateList<String>,
        view: MutableState<TeamView>
    ) {
        if (view.value.pendingMembers.contains(value))
            removetUser(value, members, view)
        else
            addMember(value, members, view)
    }

    override suspend fun addMember(
        value: User,
        members: SnapshotStateList<String>,
        view: MutableState<TeamView>
    ) {
        view.value = view.value.copy(pendingMembers = view.value.pendingMembers + value)
    }

    override suspend fun removetUser(
        value: User,
        members: SnapshotStateList<String>,
        view: MutableState<TeamView>
    ) {
        view.value = view.value.copy(pendingMembers = view.value.pendingMembers - value)
    }
}