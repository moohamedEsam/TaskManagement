package com.example.taskmanagement.presentation.screens.team

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.composables.TagComposable

@Composable
fun MemberPage(
    viewModel: TeamViewModel
) {
    val multiSelectMode by viewModel.multiSelectMode
    val members = viewModel.taggedMembersList
    if (multiSelectMode)
        MultiSelectGrid(members, viewModel)
    else
        NormalMemberGrid(members = members, viewModel = viewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectGrid(members: List<ActiveUserDto>, viewModel: TeamViewModel) {
    val selectedMembers = viewModel.selectedMembers
    val showTagDialog by viewModel.showTagDialog
    val team by viewModel.team
    var showMenu by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${selectedMembers.size} members selected",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(0.8f))
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        viewModel.toggleTagDialog()
                    }
                )
                Spacer(modifier = Modifier.padding(end = 4.dp))
                Box {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.clickable { showMenu = !showMenu }
                    )
                    if (showMenu)
                        OptionsMenu(viewModel) {
                            showMenu = false
                        }
                }
            }
        }
        items(members) { activeUserDto ->
            MemberComposable(
                user = activeUserDto.user,
                modifier = Modifier.clickable {
                    viewModel.toggleSelectedMember(activeUserDto.user)
                }
            ) {
                Spacer(modifier = Modifier.weight(0.8f))
                Checkbox(
                    checked = selectedMembers.contains(activeUserDto.user),
                    onCheckedChange = { viewModel.toggleSelectedMember(activeUserDto.user) }
                )
            }
        }
    }
    if (showTagDialog)
        TagDialog(tags = team.data?.tags ?: emptyList(), viewModel) {
            viewModel.toggleTagDialog()
        }
    BackHandler {
        viewModel.toggleMultiSelect()
    }
}

@Composable
fun OptionsMenu(viewModel: TeamViewModel, onDismiss: () -> Unit) {
    DropdownMenu(expanded = true, onDismissRequest = onDismiss) {
        DropdownMenuItem(text = { Text(text = "Remove") }, onClick = { })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagDialog(tags: List<Tag>, viewModel: TeamViewModel, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tags) {
                    TagItem(tag = it) {
                        viewModel.assignTagToSelectedMembers(it)
                        onDismiss()
                        viewModel.toggleMultiSelect()
                    }
                }
            }
        }
    }
}

@Composable
private fun NormalMemberGrid(
    members: List<ActiveUserDto>,
    viewModel: TeamViewModel
) {
    val changed by viewModel.membersTagsChanged
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members) { member ->
            MemberComposable(
                user = member.user,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            viewModel.toggleMultiSelect()
                            viewModel.toggleSelectedMember(member.user)
                        },
                        onTap = {

                        }
                    )
                }
            ) {
                if (member.tag != null)
                    TagComposable(tag = member.tag)
            }
        }
        if (changed)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { viewModel.saveTaggedMembers() }) {
                        Text(text = "Save Changes")
                    }
                }
            }
    }
}