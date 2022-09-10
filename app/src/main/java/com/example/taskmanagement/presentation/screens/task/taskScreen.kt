package com.example.taskmanagement.presentation.screens.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.customComponents.fillMaxHeight
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .verticalScroll(rememberScrollState())
    ) {
        TaskMainInfo(viewModel, navHostController)
        ActionRow(viewModel = viewModel, modifier = Modifier.align(Alignment.End))
        TaskInfoPager(viewModel = viewModel)
    }
}

@Composable
private fun ActionRow(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val updateMade by viewModel.updateMade.collectAsState()
    val show = remember {
        MutableTransitionState(updateMade)
    }
    LaunchedEffect(key1 = updateMade) {
        show.targetState = updateMade
    }
    AnimatedVisibility(
        visibleState = show,
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(
                onClick = viewModel::discardChanges,
                content = { Text(text = "Discard") }
            )

            Button(
                onClick = viewModel::saveChanges,
                content = { Text(text = "Save Changes") },
            )

        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TaskInfoPager(viewModel: TaskViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight { it / 2 },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val pages = listOf("Task Items", "Description", "Assigned", "Comments", "History")
        val pager = rememberPagerState()
        val coroutine = rememberCoroutineScope()
        ScrollableTabRow(selectedTabIndex = pager.currentPage, divider = {}) {
            pages.forEachIndexed { index, value ->
                Tab(
                    selected = pager.currentPage == index,
                    onClick = { coroutine.launch { pager.animateScrollToPage(index) } },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = value, modifier = Modifier.padding(bottom = 16.dp, end = 8.dp))
                }
            }
        }

        HorizontalPager(count = pages.size, state = pager, itemSpacing = 4.dp) { page ->
            when (page) {
                0 -> TaskItemsPage(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> TaskDescriptionPage(viewModel = viewModel)
                2 -> TaskAssignedPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                3 -> TaskCommentsPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                else -> Unit
            }
        }
    }
}

@Composable
private fun TaskDescriptionPage(viewModel: TaskViewModel) {
    val task by viewModel.task.collectAsState()
    val taskDescription by remember {
        derivedStateOf {
            task.data?.description ?: ""
        }
    }
    Text(
        text = taskDescription, modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )
}