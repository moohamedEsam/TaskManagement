package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.customComponents.CardDialog
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth

@Composable
fun GroupedMembersList(
    members: List<ActiveUserDto>,
    tags: List<Tag>,
    ratio: Int,
    showUpdateButton: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (Tag) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    val groups = members.groupBy { it.tag }.filterKeys { it != null }.toMutableMap()
    tags.forEach {
        if (!groups.containsKey(it))
            groups[it] = emptyList()
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item { Text(text = "Name") }
                item { Text(text = "Assigned") }
                item { Text(text = "View") }
                item { Text(text = "Create") }
                item { Text(text = "Edit") }
                item { Text(text = "Delete") }
            }
        }
        groups.forEach { (tag, tagMembers) ->
            item {
                GroupedMemberCard(
                    tag = tag!!,
                    members = tagMembers.map { it.user },
                    ratio
                ) { onItemClick(tag) }

            }
        }
        item {
            if (showUpdateButton)
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onUpdateButtonClick) {
                        Text(text = "Save changes")
                    }
                }

        }
    }
}

@Composable
fun MembersIcons(members: List<User>, ratio: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth { it / ratio }
    ) {
        if (members.isNotEmpty())
            UserIcon(photoPath = members[0].photoPath)
        if (members.size > 1)
            UserIcon(photoPath = members[1].photoPath)
        if (members.size > 2)
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+${members.size - 2}")
            }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupedMemberCard(
    tag: Tag,
    members: List<User>,
    ratio: Int,
    onClick: () -> Unit
) {
    ElevatedCard(modifier = Modifier.padding(8.dp), onClick = onClick) {
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { Text(text = tag.title, modifier = Modifier.fillMaxWidth { it / ratio }) }
            item { MembersIcons(members = members, ratio) }
            item {
                RadioButton(
                    selected = tag.isUserAuthorized(Permission.View),
                    onClick = { },
                    enabled = false
                )
            }
            item {
                RadioButton(
                    selected = tag.isUserAuthorized(Permission.Create),
                    onClick = { },
                    enabled = false
                )
            }

            item {
                RadioButton(
                    selected = tag.isUserAuthorized(Permission.EditName),
                    onClick = { },
                    enabled = false
                )
            }

            item {
                RadioButton(
                    selected = tag.isUserAuthorized(Permission.Delete),
                    onClick = { },
                    enabled = false
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersDialog(
    selectedMembers: List<User>,
    members: List<User>,
    onDismiss: () -> Unit,
    onToggle: (User) -> Unit
) {
    CardDialog(onDismiss = onDismiss) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                Text(
                    text = "select members to assign tag",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(members) {
                MemberComposable(user = it) {
                    Spacer(modifier = Modifier.weight(0.8f))
                    Checkbox(
                        checked = selectedMembers.contains(it),
                        onCheckedChange = { _ ->
                            onToggle(it)
                        }
                    )
                }
            }
        }
    }
}