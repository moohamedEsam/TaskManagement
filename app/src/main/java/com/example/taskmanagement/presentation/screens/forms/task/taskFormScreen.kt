package com.example.taskmanagement.presentation.screens.forms.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TaskFormScreen(
    snackbarHostState: SnackbarHostState,
    projectId: String,
    taskId: String
) {
    val viewModel: TaskFormViewModel by inject { parametersOf(projectId, taskId) }
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }

    TaskFormScreenContent(viewModel = viewModel)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TaskFormScreenContent(
    viewModel: TaskFormViewModel,
) {
    val pages = buildList {
        add("Main Data")
        add("Members")
        if (!viewModel.isUpdating)
            add("Task Items")
    }
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = page,
                        modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, end = 8.dp)
                    )
                }
            }
        }
        HorizontalPager(count = pages.size, state = pagerState) { page ->
            when (page) {
                0 -> MainTaskFromPage(viewModel = viewModel)
                1 -> AssignedList(viewModel = viewModel)
                else -> TaskItemsList(viewModel = viewModel)
            }
        }
    }
}