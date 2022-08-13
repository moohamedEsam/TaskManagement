package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.*
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.presentation.composables.GroupedMembersList
import com.example.taskmanagement.presentation.composables.MembersDialog

private const val ratio = 6

@Composable
fun TeamMembersPage(
    viewModel: TeamViewModel
) {
    val team by viewModel.team
    val taggedMembers = viewModel.taggedMembersList
    val update by viewModel.updateMade
    var showDialog by remember {
        mutableStateOf(false)
    }
    var currentTag: Tag? by remember {
        mutableStateOf(null)
    }
    GroupedMembersList(
        members = taggedMembers,
        tags = team.data?.tags ?: emptyList(),
        showUpdateButton = update,
        ratio = ratio,
        onItemClick = {
            currentTag = it
            showDialog = true
        }
    ) {
        viewModel.saveTaggedMembers()
    }
    if (showDialog)
        MembersDialog(
            selectedMembers = taggedMembers.filter { it.tag == currentTag }.map { it.user },
            members = team.data?.members?.map { it.user } ?: emptyList(),
            onDismiss = { showDialog = false }
        ) {
            viewModel.toggleMemberToTaggedMembers(it, currentTag!!)
        }

}