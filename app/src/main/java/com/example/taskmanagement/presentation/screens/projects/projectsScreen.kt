package com.example.taskmanagement.presentation.screens.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.Project
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject

@Composable
fun ProjectsScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: ProjectsViewModel by inject()
    val projects by viewModel.projects
    HandleResourceChange(
        resource = projects,
        onSuccess = {},
        snackbarHostState = snackbarHostState,
        onSnackbarClick = { viewModel.getProjects() }
    )
    projects.onSuccess {
        ProjectsScreenContent(
            projects = it,
            navHostController = navHostController
        )
    }
}

@Composable
fun ProjectsScreenContent(projects: List<Project>, navHostController: NavHostController) {
    LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
        items(projects) { project ->
            ProjectItem(
                project = project,
                navHostController = navHostController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectItem(project: Project, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navHostController.navigate("${Screens.Project.route}/${project.id}")
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = project.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.team),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    project.members.count().toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
