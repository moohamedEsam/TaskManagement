package com.example.taskmanagement.presentation.screens.forms.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
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
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(0.8f)
        ) { page ->
            when (page) {
                0 -> MainTaskFromPage(viewModel = viewModel)
                1 -> AssignedList(viewModel = viewModel)
                else -> TaskItemsList(viewModel = viewModel)
            }
        }
        SaveButton(viewModel = viewModel, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
private fun SaveButton(viewModel: TaskFormViewModel, modifier: Modifier = Modifier) {
    var showButton by remember {
        mutableStateOf(true)
    }
    val canSave by viewModel.canSave.collectAsState()
    if (showButton) {
        if (canSave)
            Button(
                onClick = {
                    viewModel.saveTask(onLoading = { showButton = false }) {
                        showButton = true
                    }
                },
                modifier = modifier
            ) {
                Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary)
            }
    } else
        CircularProgressIndicator(modifier = modifier.scale(0.5f))
}