package com.example.taskmanagement.presentation.screens.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.presentation.customComponents.*
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.team.NewProjectCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject


@Composable
fun ProjectsScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: ProjectsViewModel by inject()
    val channel = viewModel.receiveChannel
    val projects by viewModel.filteredProjects
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    ProjectsScreenContent(
        projects = projects,
        viewModel = viewModel,
        navHostController = navHostController
    )
}


@Composable
fun FilterProjectTextField(viewModel: ProjectsViewModel) {
    var value by remember {
        mutableStateOf("")
    }
    TextField(
        value = value,
        onValueChange = {
            value = it
            viewModel.filter(it)
        },
        label = { Text(text = "search projects") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ProjectsScreenContent(
    projects: List<Project>,
    viewModel: ProjectsViewModel,
    navHostController: NavHostController
) {
    Column {
        FilterProjectTextField(viewModel = viewModel)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                NewProjectCard(modifier = Modifier.fillMaxHeight { it / 3 }) {
                    navHostController.navigate(Screens.ProjectForm.withArgs(" ", " "))
                }
            }
            items(projects) { project ->
                ProjectItem(
                    project = project,
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight { it / 3 }
                )
            }
        }
    }
}

@Composable
fun ProjectItem(
    project: Project,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    OutlinedCenteredCard(
        onClick = {
            navHostController.navigate(Screens.Project.withArgs(project.id))
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2
            )
        }
    }
}
