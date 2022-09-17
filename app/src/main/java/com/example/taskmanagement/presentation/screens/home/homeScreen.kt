package com.example.taskmanagement.presentation.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.presentation.composables.TaskItem
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.example.taskmanagement.presentation.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject

@Composable
fun HomeScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: HomeViewModel by inject()
    LaunchedEffect(key1 = Unit) {
        viewModel.snackBarChannel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    HomeScreenContent(viewModel = viewModel, navHostController = navHostController)
}

@Composable
fun HomeScreenContent(viewModel: HomeViewModel, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DashboardHeader(viewModel = viewModel)
        DashboardBody(viewModel = viewModel, navHostController = navHostController)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DashboardBody(viewModel: HomeViewModel, navHostController: NavHostController) {
    val tasks by viewModel.tasks.collectAsState()
    val currentTaskStatus by viewModel.currentTaskStatus.collectAsState()
    val tasksToShow by remember {
        derivedStateOf {
            tasks.data?.filter { it.status == currentTaskStatus } ?: emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Text(
                text = "$currentTaskStatus Tasks",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(tasksToShow, key = { it.id }) { task ->
            TaskItem(task = task, modifier = Modifier
                .fillMaxWidth()
                .animateItemPlacement()
                .height(200.dp), {}) {
                navHostController.navigate(Screens.Task.withArgs(task.id))
            }
        }
    }
}

@Composable
fun DashboardHeader(viewModel: HomeViewModel) {
    val tasks by viewModel.observeTasks().collectAsState(initial = emptyMap())
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tasks.forEach {
            item {
                OverviewCardItem(
                    it.toPair(),
                    modifier = Modifier
                        .fillMaxWidth { it / 3 }
                        .height(120.dp)
                ) {
                    viewModel.onTaskStatusChanged(it.key)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OverviewCardItem(
    tasks: Pair<TaskStatus, List<Task>>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = tasks.first.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = tasks.second.size.toString(), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tasks Count", style = MaterialTheme.typography.bodySmall)
        }
    }
}