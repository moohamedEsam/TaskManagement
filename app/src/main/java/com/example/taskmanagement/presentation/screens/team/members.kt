package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.TagScope
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.CardDialog
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth
import com.example.taskmanagement.ui.theme.TaskManagementTheme

const val ratio = 6

@Composable
fun MemberPage(
    viewModel: TeamViewModel
) {
    val members = viewModel.taggedMembersList
    val team by viewModel.team

    MembersCardList(members = members, team.data?.tags ?: emptyList(), viewModel = viewModel)

}

@Composable
fun MembersCardList(members: List<ActiveUserDto>, tags: List<Tag>, viewModel: TeamViewModel) {
    val groups = members.groupBy { it.tag }.filterKeys { it != null }.toMutableMap()
    val update by viewModel.updateMade
    tags.forEach {
        if (!groups.containsKey(it))
            groups[it] = emptyList()
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
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
        groups.forEach { (tag, members) ->
            item {
                GroupedMemberCard(
                    tag = tag!!,
                    members = members.map { it.user },
                    viewModel = viewModel
                )
            }
        }
        item {
            if (update)
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { viewModel.saveTaggedMembers() }) {
                        Text(text = "Save changes")
                    }
                }

        }
    }
}

@Composable
fun MembersIcons(members: List<User>) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(ratio)) {
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
fun GroupedMemberCard(tag: Tag, members: List<User>, viewModel: TeamViewModel) {
    val team by viewModel.team
    var showDialog by remember {
        mutableStateOf(false)
    }
    ElevatedCard(modifier = Modifier.padding(8.dp), onClick = { showDialog = true }) {
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { Text(text = tag.title, modifier = Modifier.fillMaxWidth(ratio)) }
            item { MembersIcons(members = members) }
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
    if (showDialog)
        MembersDialog(team, members, viewModel, tag) { showDialog = false }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MembersDialog(
    team: Resource<TeamView>,
    members: List<User>,
    viewModel: TeamViewModel,
    tag: Tag,
    onDismiss: () -> Unit
) {
    CardDialog(onDismiss = onDismiss) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                Text(
                    text = "select members to assign tag",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(team.data?.members ?: emptyList()) {
                MemberComposable(user = it.user) {
                    Checkbox(
                        checked = members.contains(it.user),
                        onCheckedChange = { _ ->
                            viewModel.toggleMemberToTaggedMembers(it.user, tag)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MembersPreview() {
    TaskManagementTheme {
        val tag = Tag(
            listOf(Permission.FullControl),
            "Project Manager",
            listOf(0f, 0f, 0f),
            TagScope.Team,
            "",
            ""
        )
        val members = buildList {
            val user = User("", "", null, null, "")
            add(ActiveUserDto(user, tag))
            add(ActiveUserDto(user, tag))
            add(ActiveUserDto(user, tag))
            add(ActiveUserDto(user, tag))
            add(ActiveUserDto(user, tag))
            add(ActiveUserDto(user, tag))
        }
    }
}