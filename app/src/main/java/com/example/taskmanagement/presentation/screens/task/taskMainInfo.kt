package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.forms.task.TaskFormViewModel
import com.example.taskmanagement.ui.theme.TaskManagementTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ColumnScope.TaskMainInfo(
    viewModel: TaskViewModel,
    navHostController: NavHostController
) {
    val task by viewModel.task.collectAsState()
    val taskView by remember {
        derivedStateOf {
            task.data ?: TaskView("")
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = taskView.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge
        )

        ActionRow(
            navHostController = navHostController,
            taskView = taskView
        )
    }
    TaskMainInfoContent(taskView)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskMainInfoContent(
    task: TaskView
) {
    val simpleDateFormat = remember {
        SimpleDateFormat("yy MMM dd")
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        TaskMainInfoValue(icon = Icons.Default.Tag, label = "Status: ") {
            TextButton(onClick = { }) {
                Text(text = task.status.toString(), textDecoration = TextDecoration.None)
            }
        }
        if (task.finishDate != null)
            TaskMainInfoValue(icon = Icons.Default.Alarm, label = "DeadLine: ") {
                Text(
                    text = simpleDateFormat.format(task.finishDate),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        if (task.estimatedTime != null)
            TaskMainInfoValue(icon = Icons.Default.CalendarMonth, label = "estimated Time: ") {
                Text(
                    text = task.estimatedTime.toString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        TaskMainInfoValue(icon = Icons.Default.PriorityHigh, label = "Priority: ") {
            Text(text = task.priority.toString(), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ActionRow(
    navHostController: NavHostController,
    taskView: TaskView,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        IconButton(
            onClick = {
                navHostController.navigate(Screens.TaskForm.withArgs(taskView.project, taskView.id))
            }
        ) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
        }
    }
}


@Composable
private fun TaskMainInfoValue(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
        Text(text = label, color = Color.Gray)
        Spacer(modifier = Modifier.width(32.dp))
        content()
    }
}

@Preview
@Composable
fun temp() {
    TaskManagementTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TaskMainInfoContent(task = TaskView("test task", finishDate = Date()))
        }
    }
}