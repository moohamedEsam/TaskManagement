package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.layout.Box
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
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.user.User

private const val ratio = 6

@Composable
fun GroupedMembers(
    tags: List<Tag>,
    members: List<User>,
    update: Boolean,
    taggedMembers: List<ActiveUserDto>,
    onUpdateButtonClick: () -> Unit,
    onFloatingButtonClick: () -> Unit,
    onDialogMemberToggle: (User, Tag) -> Unit
) {
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
    Box {
        GroupedMembersList(
            members = taggedMembers,
            tags = tags,
            showUpdateButton = update,
            ratio = ratio,
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            onItemClick = {
                currentTag = it
                showDialog = true
            },
            onUpdateButtonClick = onUpdateButtonClick
        )
        ExtendedFloatingActionButton(
            onClick = onFloatingButtonClick,
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
            text = { Text(text = "Create New Tag") },
            expanded = expanded,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
    if (showDialog)
        MembersDialog(
            selectedMembers = taggedMembers.filter { it.tag == currentTag }.map { it.user },
            members = members,
            onDismiss = { showDialog = false },
            onToggle = { onDialogMemberToggle(it, currentTag!!) }
        )
}