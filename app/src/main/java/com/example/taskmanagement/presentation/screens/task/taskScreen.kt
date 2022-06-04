package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.TaskItem
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat

@Composable
fun TaskScreen(
    navHostController: NavHostController,
    taskId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel by inject<TaskViewModel> { parametersOf(taskId) }
    val taskResource by viewModel.task
    val task = taskResource.data ?: return
    HandleResourceChange(
        resource = taskResource,
        onSuccess = {},
        snackbarHostState = snackbarHostState,
        onSnackbarClick = {
            viewModel.getTask()
        })
    Box {
        TaskScreenContent(navHostController, task)

    }
}

@Composable
private fun TaskScreenContent(
    navHostController: NavHostController,
    task: TaskView
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ActionRow(navHostController)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = task.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.padding(16.dp))
        TaskMainInfo(task)
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Priority", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = task.priority.toString(), style = MaterialTheme.typography.bodyLarge)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Status", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = task.status.toString(), style = MaterialTheme.typography.bodyLarge)
            }

        }
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Description", style = MaterialTheme.typography.bodyLarge)
        Text(text = task.description ?: "", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "task items", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.padding(8.dp))
        LazyColumn {
            items(task.taskItems ?: emptyList()) {
                TaskItemCard(it)
            }
        }
    }
}

@Composable
private fun TaskMainInfo(task: TaskView) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            if (task.completeDate != null) {
                Text(text = "complete Date", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = SimpleDateFormat.getDateInstance().format(task.completeDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopCenter)
                .width(1.dp)
        )

        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            if (task.finishDate != null) {
                Text(text = "Finish Date", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = SimpleDateFormat.getDateInstance().format(task.finishDate),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ActionRow(navHostController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemCard(taskItem: TaskItem) {
    OutlinedCard(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(selected = taskItem.completed) {

            }
            Text(text = taskItem.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
