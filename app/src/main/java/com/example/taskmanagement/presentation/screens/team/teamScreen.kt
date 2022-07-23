package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.navigation.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
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

    HandleResourceChange(
        resource = team,
        onSuccess = { },
        snackbarHostState = snackbarHostState,
        onSnackbarClick = { viewModel.getTeam() }
    )
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
    val pages = listOf("main", "members", "tags", "options")
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
                0 -> MainPage(
                    navHostController = navHostController,
                    viewModel = viewModel,
                    team = team
                )
                1 -> MemberPage(members = team.members, viewModel = viewModel)
                2 -> TagPage(tags = team.tags, viewModel = viewModel)
                else -> Box {}
            }
        }
    }
}

@Composable
fun MainPage(navHostController: NavHostController, team: TeamView, viewModel: TeamViewModel) {
    val projectHeaders =
        listOf("Name", "Members", "Created Tasks", "Completed Tasks", "In Progress")
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //TeamHeader(team = team, navHostController = navHostController)
        LazyColumn(modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 3).dp)) {
            item {
                Text(
                    text = "Projects",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                Divider()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    itemsIndexed(projectHeaders) { index, value ->
                        Text(text = value, style = MaterialTheme.typography.headlineMedium)
                        if (index == 0)
                            Spacer(modifier = Modifier.width(64.dp))
                    }
                }
                Divider()
            }

        }
    }
}

@Composable
fun MemberPage(members: List<ActiveUserDto>, viewModel: TeamViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members) {
            MemberComposable(user = it.user, action = { }, onClick = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagPage(tags: List<Tag>, viewModel: TeamViewModel) {
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) {
                Column {
                    SuggestionChip(
                        onClick = { },
                        label = { Text(text = it.title) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(
                                it.color
                            )
                        )
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(it.permissions) {
                            Text(text = it.toString(), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun ActionHeader(navHostController: NavHostController, team: TeamView) {
    Row(
        horizontalArrangement = Arrangement.End,
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navHostController.navigate(Screens.TeamForm.withArgs(team.id))
                }
        )
    }
}

@Composable
fun TeamHeader(team: TeamView, navHostController: NavHostController) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            Text(text = team.name, style = MaterialTheme.typography.titleLarge)
            ActionHeader(navHostController = navHostController, team = team)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = team.description ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}
