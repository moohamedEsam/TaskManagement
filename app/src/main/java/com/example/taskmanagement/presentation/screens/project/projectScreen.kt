package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun ProjectScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    projectId: String
) {
    val viewModel: ProjectViewModel by inject { parametersOf(projectId) }
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }

    ProjectScreenContent(viewModel = viewModel, navHostController = navHostController)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectScreenContent(viewModel: ProjectViewModel, navHostController: NavHostController) {
    val pages = listOf("Dashboard", "Tags", "Members", "Timeline", "Options")
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()
    Column(modifier = Modifier.padding(8.dp)) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
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
                        modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                    )
                }
            }
        }
        HorizontalPager(count = pages.size, state = pagerState) { page ->
            when (page) {
                0 -> TasksPage(navHostController, viewModel)
                1 -> ProjectGroupedMembers(viewModel, navHostController)
                2 -> ProjectMembersPage(
                    viewModel = viewModel,
                    navHostController = navHostController
                )
                3 -> ProjectTimeLine(viewModel = viewModel, navHostController = navHostController)
                else -> OptionsPage()
            }
        }
    }
}













