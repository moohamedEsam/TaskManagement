package com.example.taskmanagement.presentation.screens.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.composables.GroupedMembers
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun ProjectGroupedMembers(viewModel: ProjectViewModel, navHostController: NavHostController) {
    val projectView by viewModel.project
    val taggedMembers = viewModel.taggedMembers
    val update by viewModel.updateMade
    projectView.onSuccess {
        GroupedMembers(
            tags = it.tags,
            members = it.members.map { activeUser -> activeUser.user },
            update = update,
            taggedMembers = taggedMembers,
            onUpdateButtonClick = { viewModel.saveTaggedMembers() },
            onFloatingButtonClick = {
                navHostController.navigate(
                    Screens.TagForm.withArgs(
                        it.id,
                        "projects"
                    )
                )
            },
            onDialogMemberToggle = { user, tag -> viewModel.toggleMemberToTaggedMembers(user, tag) }
        )
    }
}