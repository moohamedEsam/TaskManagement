package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.project.AbstractProject
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TeamScreen(
    navHostController: NavHostController,
    teamId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TeamViewModel by inject { parametersOf(teamId) }
    val team by viewModel.team

    HandleResourceChange(
        resource = team,
        onSuccess = { },
        snackbarHostState = snackbarHostState,
        onSnackbarClick = { viewModel.getTeam() }
    )
    team.onSuccess {
        TeamScreenContent(
            team = it,
            navHostController = navHostController
        )
    }
}

@Composable
fun TeamScreenContent(
    team: TeamView,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TeamHeader(team = team, navHostController)
        //MembersList(users = team.members.map { it.user }, navHostController = navHostController)
        TeamFooter(team = team, navHostController = navHostController)
    }
}

@Composable
fun ActionHeader(navHostController: NavHostController, team: TeamView) {
    Row(
        horizontalArrangement = Arrangement.End,
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navHostController.navigate(Screens.TeamForm.withArgs(team.id))
                }
        )
    }
}

@Composable
fun TeamFooter(team: TeamView, navHostController: NavHostController) {
    Column {
        Text("Projects", modifier = Modifier.padding(4.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
            items(team.projects) {
                ProjectCard(it)
            }
            item {
                IconButton(onClick = {
                    navHostController.navigate(Screens.ProjectForm.withArgs(team.id, "  "))
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectCard(it: AbstractProject) {
    OutlinedCard(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = it.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun TeamHeader(team: TeamView, navHostController: NavHostController) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            Text(text = team.name, style = MaterialTheme.typography.titleLarge)
            ActionHeader(navHostController = navHostController, team = team)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = team.description ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}
