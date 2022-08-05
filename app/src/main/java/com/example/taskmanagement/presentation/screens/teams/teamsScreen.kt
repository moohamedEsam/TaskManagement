package com.example.taskmanagement.presentation.screens.teams

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.presentation.customComponents.ElevatedCenteredCard
import com.example.taskmanagement.presentation.customComponents.OutlinedCenteredCard
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject

@Composable
fun TeamsScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: TeamsViewModel by inject()
    TeamsScreenContent(navHostController, viewModel)
}

@Composable
fun TeamsScreenContent(navHostController: NavHostController, viewModel: TeamsViewModel) {
    val teams by viewModel.teams
    val searchQuery by viewModel.searchQuery
    val filteredTeams by viewModel.filteredTeams
    val configuration = LocalConfiguration.current
    val ratio = 4
    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
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
            columns = GridCells.Adaptive(120.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NewTeamCard(configuration, ratio, navHostController)
            }
            items(if (searchQuery.isBlank()) teams else filteredTeams) {
                TeamCard(configuration, ratio, navHostController, it)
            }
        }
    }
}

@Composable
private fun NewTeamCard(
    configuration: Configuration,
    ratio: Int,
    navHostController: NavHostController
) {
    OutlinedCenteredCard(
        modifier = Modifier.size(
            (configuration.screenWidthDp / ratio).dp,
            (configuration.screenHeightDp / ratio).dp
        ),
        onClick = {
            navHostController.navigate(Screens.TeamForm.withArgs("   "))
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
    configuration: Configuration,
    ratio: Int,
    navHostController: NavHostController,
    team: Team
) {
    ElevatedCenteredCard(
        modifier = Modifier.size(
            (configuration.screenWidthDp / ratio).dp,
            (configuration.screenHeightDp / ratio).dp
        ),
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
