package com.example.taskmanagement.presentation.screens.teams

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.customComponents.ElevatedCenteredCard
import com.example.taskmanagement.presentation.customComponents.OutlinedCenteredCard
import com.example.taskmanagement.presentation.customComponents.fillMaxHeight
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.example.taskmanagement.presentation.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject

@Composable
fun TeamsScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: TeamsViewModel by inject()
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    TeamsScreenContent(navHostController, viewModel)
}

@Composable
fun TeamsScreenContent(navHostController: NavHostController, viewModel: TeamsViewModel) {
    val teams by viewModel.teams
    val searchQuery by viewModel.searchQuery
    val filteredTeams by viewModel.filteredTeams
    val ratio = 4
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text(text = "Search teams") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) }
        )
        Text(text = "Teams", style = MaterialTheme.typography.headlineLarge)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                NewTeamCard(
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio)
                )
            }
            items(if (searchQuery.isBlank()) teams else filteredTeams) {
                TeamCard(
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio),
                    team = it
                )
            }
        }
    }
}

@Composable
private fun NewTeamCard(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    OutlinedCenteredCard(
        modifier = modifier,
        onClick = {
            navHostController.navigate(Screens.TeamForm.withArgs("   "))
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text(
                text = "New Team",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun TeamCard(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    team: Team
) {
    ElevatedCenteredCard(
        modifier = modifier,
        onClick = { navHostController.navigate(Screens.Team.withArgs(team.id)) }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = team.description ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 2.dp, vertical = 4.dp)
                .align(Alignment.TopEnd)
        )
    }
}