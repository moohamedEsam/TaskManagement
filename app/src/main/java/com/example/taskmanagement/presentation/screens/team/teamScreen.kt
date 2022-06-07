package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.abstarct.AbstractProject
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.presentation.composables.ShowDialogFormButton
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.customComponents.MembersList
import com.example.taskmanagement.presentation.screens.forms.ProjectFormScreen
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
            viewModel = viewModel,
            navHostController = navHostController
        )
    }
}

@Composable
fun TeamScreenContent(
    team: TeamView,
    viewModel: TeamViewModel,
    navHostController: NavHostController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TeamHeader(team = team)
        Spacer(Modifier.height(16.dp))
        MembersList(users = team.members, navHostController = navHostController)
        Spacer(modifier = Modifier.height(16.dp))
        TeamFooter(team = team, viewModel = viewModel)
    }
}

@Composable
fun TeamFooter(team: TeamView, viewModel: TeamViewModel) {
    Column {
        Text("Projects", modifier = Modifier.padding(4.dp))

        LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
            items(team.projects) {
                ProjectCard(it)
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
fun TeamHeader(team: TeamView) {
    Column {
        Text(text = team.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = team.description ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}
