package com.example.taskmanagement.presentation.screens.project

import androidx.compose.runtime.*
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.presentation.composables.GroupedMembersList
import com.example.taskmanagement.presentation.composables.MembersDialog

private const val ratio = 6

@Composable
fun ProjectMembersPage(viewModel: ProjectViewModel) {
    val project by viewModel.project
    val taggedMembers = viewModel.taggedMembers
    val update by viewModel.updateMade
    var showDialog by remember {
        mutableStateOf(false)
    }
    var currentTag: Tag? by remember {
        mutableStateOf(null)
    }
    GroupedMembersList(
        members = taggedMembers,
        tags = project.data?.tags ?: emptyList(),
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
            members = project.data?.members?.map { it.user } ?: emptyList(),
            onDismiss = { showDialog = false }
        ) {
            viewModel.toggleMemberToTaggedMembers(it, currentTag!!)
        }
}