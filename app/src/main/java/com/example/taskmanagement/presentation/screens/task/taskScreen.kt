package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TaskScreen(
    navHostController: NavHostController,
    taskId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel by inject<TaskViewModel> { parametersOf(taskId) }
    LaunchedEffect(key1 = Unit) {
        viewModel.receiveChannel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    TaskScreenContent(navHostController = navHostController, viewModel = viewModel)
}

@Composable
private fun TaskScreenContent(
    navHostController: NavHostController,
    viewModel: TaskViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskMainInfo(viewModel, navHostController)
            TaskInfoPager(viewModel = viewModel)
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TaskInfoPager(viewModel: TaskViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val pages = listOf("Task Items", "Description", "Assigned", "Comments", "History")
        val pager = rememberPagerState()
        val coroutine = rememberCoroutineScope()
        ScrollableTabRow(selectedTabIndex = pager.currentPage, divider = {}) {
            pages.forEachIndexed { index, value ->
                Tab(
                    selected = pager.currentPage == index,
                    onClick = { coroutine.launch { pager.animateScrollToPage(index) } }
                ) {
                    Text(text = value, modifier = Modifier.padding(bottom = 16.dp, end = 8.dp))
                }
            }
        }

        HorizontalPager(count = pages.size, state = pager, itemSpacing = 4.dp) { page ->
            when (page) {
                0 -> Unit
                1 -> Unit
                2 -> Unit
                3 -> Unit
                else -> Unit
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskItemCard(taskItem: TaskItem) {
    OutlinedCard(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(selected = taskItem.isCompleted) {

            }
            Text(text = taskItem.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
