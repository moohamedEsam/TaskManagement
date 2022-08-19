package com.example.taskmanagement.presentation.screens.team

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.project.ProjectSummery
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.*
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun DashBoardPage(navHostController: NavHostController, team: TeamView, viewModel: TeamViewModel) {
    val ratio = 3
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.weight(0.8f))
            SharerIcon(viewModel = viewModel)
            EditIcon(navHostController, team, viewModel)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team.description ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .verticalScroll(rememberScrollState())
            )
            TaskPie(team = team)
        }


        Text(
            text = "Projects",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(3)
        ) {
            item {
                NewProjectCard(
                    team = team,
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio)
                )
            }

            items(team.projects) {
                ProjectCard(
                    team = team,
                    projectSummery = it,
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio)
                )
            }
        }

    }
}

@Composable
private fun SharerIcon(
    viewModel: TeamViewModel
) {
    val visible = remember {
        MutableTransitionState(!viewModel.isShareIconVisible()).apply {
            targetState = viewModel.isShareIconVisible()
        }
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(animationSpec = tween(2000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        IconButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
        }
    }
    if (showDialog)
        InvitationsDialog(viewModel = viewModel) {
            showDialog = false
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsDialog(viewModel: TeamViewModel, onDismiss: () -> Unit) {
    var value by remember {
        mutableStateOf("")
    }
    val invitation by viewModel.invitations
    val suggestions by viewModel.suggestions
    CardDialog(onDismiss = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = it
                    if (it.length > 2 && it.isNotBlank())
                        viewModel.searchUsers(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Search by username or email") }
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxHeight(2)
            ) {
                items(suggestions) {
                    MemberComposable(user = it) {
                        Checkbox(
                            checked = invitation.contains(it),
                            onCheckedChange = { _ -> viewModel.toggleUserToInvitations(it) }
                        )
                    }
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(invitation.toList()) {
                    FilterChip(
                        selected = true,
                        onClick = {},
                        label = { Text(text = it.username) })
                }
            }

            Button(onClick = { viewModel.sendInvitations() }) {
                Text(text = "Send")
            }
        }
    }
}

@Composable
private fun EditIcon(
    navHostController: NavHostController,
    team: TeamView,
    viewModel: TeamViewModel
) {
    val visible = remember {
        MutableTransitionState(!viewModel.isEditIconVisible()).apply {
            targetState = viewModel.isEditIconVisible()
        }
    }
    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(animationSpec = tween(2000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        IconButton(
            onClick = {
                navHostController.navigate(Screens.TeamForm.withArgs(team.id))
            },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }
    }
}

@Composable
fun ProjectCard(
    team: TeamView,
    projectSummery: ProjectSummery,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    ElevatedCenteredCard(
        modifier = modifier,
        onClick = {
            navHostController.navigate(Screens.Project.withArgs(projectSummery.id))
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = projectSummery.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            ProjectTaskOverall(projectSummery)
        }
    }
}

@Composable
private fun ColumnScope.ProjectTaskOverall(projectSummery: ProjectSummery) {
    if (projectSummery.createdTasks == 0)
        return
    Text(
        text = "Completed Tasks (${projectSummery.completedTasks * 100 / projectSummery.createdTasks})%",
        color = Color.Green
    )
    Text(
        text = "InProgress Tasks (${projectSummery.inProgressTasks * 100 / projectSummery.createdTasks})%",
        color = Color.Yellow
    )
    Text(
        text = "Late Tasks (${(projectSummery.createdTasks - projectSummery.completedTasks - projectSummery.inProgressTasks) * 100 / projectSummery.createdTasks})%",
        color = Color.Red
    )
    Text(
        text = "Members: ${projectSummery.members.size}",
        modifier = Modifier.Companion
            .align(Alignment.End)
            .padding(vertical = 16.dp)
    )

}

@Composable
fun NewProjectCard(
    team: TeamView,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    OutlinedCenteredCard(
        modifier = modifier,
        onClick = { navHostController.navigate(Screens.ProjectForm.withArgs(team.id, " ")) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text(text = "New Project", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun TaskPie(team: TeamView, modifier: Modifier = Modifier) {
    val created = team.projects.sumOf { it.createdTasks }
    if (team.projects.isEmpty() || created == 0) {
        Column(modifier = modifier) { DrawDonat(Color.Black to 1) }
        return
    }

    val completed = team.projects.sumOf { it.completedTasks }
    val inProgress = team.projects.sumOf { it.inProgressTasks }
    val late = created - completed - inProgress
    Column(modifier = modifier) {
        Text(
            text = "Task Overall",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            DrawDonat(
                Color.Red to late,
                Color.Yellow to inProgress,
                Color.Green to completed,
                modifier = Modifier.size(maxWidth, maxHeight.div(1.9f))
            )
            Text(text = "${team.projects.sumOf { it.createdTasks }}")
        }
        Text(text = "Completed ${completed * 100 / created}%", color = Color.Green)
        Text(text = "InProgress ${inProgress * 100 / created}%", color = Color.Yellow)
        Text(text = "Late ${late * 100 / created}%", color = Color.Red)
    }
}