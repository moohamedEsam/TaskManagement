package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.presentation.composables.TaskItem
import com.example.taskmanagement.presentation.customComponents.*
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.projects.ProjectItem

private const val ratio = 3

@Composable
fun TasksPage(navHostController: NavHostController, viewModel: ProjectViewModel) {
    val project by viewModel.project
    project.onSuccess {
        DashboardContent(it, navHostController)
    }
    project.onError {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Icon(imageVector = Icons.Default.Error, contentDescription = null)
                Text(text = it ?: "")
            }

        }
    }
}

@Composable
private fun DashboardContent(
    project: ProjectView,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(0.8f))
            IconButton(
                onClick = {
                    navHostController.navigate(
                        Screens.ProjectForm.withArgs(
                            project.team,
                            project.id
                        )
                    )
                }
            ) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
            }

            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .verticalScroll(rememberScrollState())
            )
            TaskPie(
                createdTasks = project.tasks.count(),
                completedTasks = project.tasks.count { it.status == TaskStatus.Completed },
                inProgressTasks = project.tasks.count { it.status == TaskStatus.InProgress }
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            item {
                TasksHeader(navHostController, project)

            }
            items(project.tasks) { task ->
                TaskItem(
                    task = task,
                    onCompleteClick = {},
                    modifier = Modifier.fillMaxHeight { it / ratio }
                ) {
                    navHostController.navigate(Screens.Task.withArgs(task.id))
                }
            }
        }
    }
}

@Composable
private fun TasksHeader(
    navHostController: NavHostController,
    project: ProjectView
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
        IconButton(onClick = {
            navHostController.navigate(
                Screens.TaskForm.withArgs(
                    project.id,
                    " "
                )
            )
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}