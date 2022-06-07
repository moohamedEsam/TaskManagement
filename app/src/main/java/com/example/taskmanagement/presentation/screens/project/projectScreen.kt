package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.TaskStatus
import com.example.taskmanagement.domain.dataModels.abstarct.AbstractTask
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.customComponents.MembersList
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun ProjectScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    projectId: String
) {
    val viewModel: ProjectViewModel by inject {
        parametersOf(projectId)
    }
    val project by viewModel.project

    HandleResourceChange(
        resource = project,
        onSuccess = { },
        snackbarHostState = snackbarHostState,
        onSnackbarClick = {
            viewModel.getProject()
        }
    )
    project.onSuccess {
        ProjectScreenContent(
            project = it,
            viewModel = viewModel,
            navHostController = navHostController,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun ProjectScreenContent(
    project: ProjectView,
    viewModel: ProjectViewModel,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ProjectScreenHeader(project = project)
        Spacer(modifier = Modifier.height(8.dp))
        MembersList(users = project.members, navHostController = navHostController)
        Spacer(modifier = Modifier.height(8.dp))
        ProjectScreenTasks(tasks = project.tasks, navHostController = navHostController)
        Spacer(modifier = Modifier.weight(0.8f))
    }
}

@Composable
fun ProjectScreenTasks(tasks: List<AbstractTask>, navHostController: NavHostController) {
    Column {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(tasks) {
                TaskCard(task = it, navHostController = navHostController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: AbstractTask, navHostController: NavHostController) {
    OutlinedCard(
        modifier = Modifier
            .padding(8.dp)
            .width(IntrinsicSize.Max)
            .clickable {
                navHostController.navigate("${Screens.Task.route}/${task.publicId}")
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = task.priority.toString(),
                    color = when (task.priority) {
                        Priority.High -> Color.Red
                        Priority.Medium -> Color.Yellow
                        else -> Color.Green
                    }
                )
                Text(
                    text = task.status.toString(),
                    color = when (task.status) {
                        TaskStatus.Completed -> Color.Green
                        TaskStatus.InProgress -> Color.Yellow
                        TaskStatus.Late -> Color.Red
                        else -> Color.Black
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectScreenHeader(project: ProjectView) {
    Column {
        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        Text(text = project.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = project.description, style = MaterialTheme.typography.bodyLarge)
    }
}
