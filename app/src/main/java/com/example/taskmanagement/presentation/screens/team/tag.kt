package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.composables.GroupedMembers
import com.example.taskmanagement.presentation.navigation.Screens


@Composable
fun TeamGroupedMembersPage(
    viewModel: TeamViewModel,
    navHostController: NavHostController
) {
    val team by viewModel.team
    val taggedMembers = viewModel.taggedMembersList
    val update by viewModel.updateMade
    team.onSuccess {
        GroupedMembers(
            tags = it.tags,
            members = it.members.map { activeUserDto -> activeUserDto.user },
            update = update,
            taggedMembers = taggedMembers,
            onUpdateButtonClick = { viewModel.saveTaggedMembers() },
            onFloatingButtonClick = {
                navHostController.navigate(
                    Screens.TagForm.withArgs(
                        it.id,
                        "teams"
                    )
                )
            },
            onDialogMemberToggle = { user, tag -> viewModel.toggleMemberToTaggedMembers(user, tag) }
        )
    }
}