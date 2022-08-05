package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun DashBoardPage(navHostController: NavHostController, team: TeamView, viewModel: TeamViewModel) {
    val configuration = LocalConfiguration.current
    val ratio = 3
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.headlineLarge
            )
            IconButton(
                onClick = {
                    navHostController.navigate(Screens.TeamForm.withArgs(team.id))
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
        Text(text = team.description ?: "", style = MaterialTheme.typography.bodyLarge)

        Text(
            text = "Projects",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
            item {
                NewProjectCard(
                    team = team,
                    navHostController = navHostController,
                    configuration = configuration,
                    ratio = ratio
                )
            }

            items(team.projects) {
                ProjectCard(
                    projectSummery = it,
                    navHostController = navHostController,
                    configuration = configuration,
                    ratio = ratio
                )
            }
        }

    }
}