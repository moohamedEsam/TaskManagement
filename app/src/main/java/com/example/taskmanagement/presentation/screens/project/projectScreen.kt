package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.team.TagPage
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
    val project by viewModel.project
    val pages = listOf("Tasks", "Members", "Tags", "Options")
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()
    Column {
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
                0 -> TasksPage(navHostController, viewModel)
                1 -> ProjectMembersPage(viewModel)
                2 -> TagPage(tags = project.data?.tags ?: emptyList()) {
                    navHostController.navigate(
                        Screens.TagForm.withArgs(
                            project.data?.id ?: "",
                            ParentRoute.Projects
                        )
                    )
                }
                else -> OptionsPage()
            }
        }
    }
}













