package com.example.taskmanagement.presentation.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.presentation.composables.TaskItem
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.ui.theme.TaskManagementTheme
import org.koin.androidx.compose.get

@Composable
fun HomeScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: HomeViewModel = get()
    val tasks by viewModel.tasks
    TaskResourceHandler(tasks, snackbarHostState, viewModel)
    HomeScreenContent(viewModel, navHostController)
}

@Composable
private fun HomeScreenContent(
    viewModel: HomeViewModel,
    navHostController: NavHostController
) {
    val tasks by viewModel.tasks
    val filteredTasks by viewModel.filteredTasks
    val searchMode by viewModel.searchMode
    Column {
        TaskFilterHandler(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        TaskList(if (searchMode) filteredTasks else tasks.data ?: emptyList(), navHostController)
    }
}

@Composable
private fun TaskResourceHandler(
    tasks: Resource<List<Task>>,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel
) {
    HandleResourceChange(
        resource = tasks,
        onSuccess = { },
        snackbarHostState = snackbarHostState,
        onSnackbarClick = {
            viewModel.getUserTasks()
        }
    )
}

@Composable
fun TaskFilterHandler(viewModel: HomeViewModel) {
    var showFilter by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.animateContentSize()
    ) {
        if (showFilter)
            TasksFilters(viewModel) {
                showFilter = !it
            }
        else
            TaskFilterDropDown(viewModel::clearFilters) {
                showFilter = it
            }
    }
}

@Composable
fun TaskFilterDropDown(
    onClearClick: () -> Unit,
    onChecked: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Filters")
        Spacer(modifier = Modifier.weight(0.6f))

        OutlinedButton(onClick = onClearClick) {
            Text(text = "Clear Filters")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconToggleButton(checked = false, onCheckedChange = onChecked) {
            Icon(Icons.Filled.FilterList, null)
        }
    }
}

@Composable
fun TasksFilters(
    viewModel: HomeViewModel,
    onChecked: (Boolean) -> Unit
) {
    Column {
        FilterSearchTextField(viewModel)
        FilterChipRow(viewModel.priorityFilterStates, "Priority") { priority, value ->
            viewModel.setFilterState(priority, value)
        }
        FilterChipRow(viewModel.statusFilterStates, "Status") { status, value ->
            viewModel.setFilterState(status, value)
        }
        CloseFilterRow(onChecked)
    }
}

@Composable
private fun FilterSearchTextField(viewModel: HomeViewModel) {
    val searchQuery by viewModel.searchQuery
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.setSearchQuery(it) },
        label = { Text("Search") },
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        leadingIcon = { Icon(Icons.Filled.Search, null) }
    )
}


@Composable
private fun CloseFilterRow(onChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .clickable {
                onChecked(true)
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> FilterChipRow(
    chipList: SnapshotStateMap<T, Boolean>,
    title: String,
    onClick: (T, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow {
            items(chipList.map { it.key }) {
                FilterChip(
                    selected = chipList[it]!!,
                    onClick = {
                        onClick(it, !chipList[it]!!)
                    },
                    label = { Text(it.toString()) },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    navHostController: NavHostController
) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(90.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(tasks) {
            TaskItem(task = it, onCompleteClick = {}) {
                navHostController.navigate("${Screens.Task.route}/${it.id}")
            }
        }
    }
}


@Preview
@Composable
fun HomePreview() {
    TaskManagementTheme {

    }
}