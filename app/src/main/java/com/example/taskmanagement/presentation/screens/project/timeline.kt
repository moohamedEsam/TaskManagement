package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth
import com.example.taskmanagement.presentation.navigation.Screens
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectTimeLine(viewModel: ProjectViewModel, navHostController: NavHostController) {
    val project by viewModel.project
    project.onSuccess {
        ProjectTimeLineContent(viewModel = viewModel, navHostController = navHostController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectTimeLineContent(viewModel: ProjectViewModel, navHostController: NavHostController) {
    val groupedTasks = remember {
        viewModel.getGroupedTasks()
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        groupedTasks.forEach { (date, tasks) ->
            stickyHeader {
                Divider()
                Text(
                    text = date,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }
            items(tasks, key = { it.id }) { task ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .clickable { navHostController.navigate(Screens.Task.withArgs(task.id)) },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (task.status == TaskStatus.Completed)
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        else
                            Icon(imageVector = Icons.Outlined.Circle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = task.title)
                    }
                    if (task.description != null)
                        Text(text = task.description)
                    Text(text = "Members: ${task.assigned.size}")
                    Divider()
                }
            }
        }
    }
}