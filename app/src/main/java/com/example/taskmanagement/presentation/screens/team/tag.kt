package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.presentation.composables.GroupedMembersList
import com.example.taskmanagement.presentation.composables.MembersDialog
import com.example.taskmanagement.presentation.navigation.Screens
import kotlin.math.exp

private const val ratio = 6

@Composable
fun TeamGroupedMembersPage(
    viewModel: TeamViewModel,
    navHostController: NavHostController
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
    var expanded by remember {
        mutableStateOf(true)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                expanded = available.y > 0
                return super.onPreScroll(available, source)
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GroupedMembersList(
            members = taggedMembers,
            tags = team.data?.tags ?: emptyList(),
            showUpdateButton = update,
            ratio = ratio,
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            onItemClick = {
                currentTag = it
                showDialog = true
            }
        ) {
            viewModel.saveTaggedMembers()
        }
        ExtendedFloatingActionButton(
            onClick = {
                navHostController.navigate(Screens.TagForm.withArgs(team.data?.id ?: "", "teams"))
            },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
            text = { Text(text = "Create New Tag") },
            expanded = expanded,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
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