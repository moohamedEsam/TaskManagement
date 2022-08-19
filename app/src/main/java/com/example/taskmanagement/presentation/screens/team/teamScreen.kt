package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.example.taskmanagement.presentation.navigation.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TeamScreen(
    navHostController: NavHostController,
    teamId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TeamViewModel by inject { parametersOf(teamId) }
    val team by viewModel.team
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }

    team.onSuccess {
        TeamScreenContent(navHostController = navHostController, viewModel = viewModel, team = it)
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamScreenContent(
    navHostController: NavHostController,
    team: TeamView,
    viewModel: TeamViewModel
) {
    val pages = listOf("Dashboard", "Tags", "Members", "Options")
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
                0 -> DashBoardPage(
                    navHostController = navHostController,
                    viewModel = viewModel,
                    team = team
                )
                1 -> TeamGroupedMembersPage(viewModel = viewModel, navHostController=navHostController)
                2 -> TeamMemberPage(team, viewModel)
                else -> Box {}
            }
        }
    }
}