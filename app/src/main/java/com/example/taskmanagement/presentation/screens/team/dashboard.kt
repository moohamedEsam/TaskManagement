package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.project.ProjectSummery
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.customComponents.ElevatedCenteredCard
import com.example.taskmanagement.presentation.customComponents.OutlinedCenteredCard
import com.example.taskmanagement.presentation.customComponents.fillMaxHeight
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun DashBoardPage(navHostController: NavHostController, team: TeamView, viewModel: TeamViewModel) {
    val ratio = 4
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                NewProjectCard(
                    team = team,
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio)
                )
            }

            items(team.projects) {
                ProjectCard(
                    team = team,
                    projectSummery = it,
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxHeight(ratio)
                )
            }
        }

    }
}

@Composable
fun ProjectCard(
    team: TeamView,
    projectSummery: ProjectSummery,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    ElevatedCenteredCard(
        modifier = modifier,
        onClick = {
            navHostController.navigate(Screens.Project.withArgs(projectSummery.id))
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = projectSummery.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )

//            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
//                Icon(
//                    imageVector = Icons.Default.Circle,
//                    contentDescription = null,
//                    tint = Color.Green
//                )
//                Text(text = "Completed Tasks (${projectSummery.completedTasks})")
//                Icon(
//                    imageVector = Icons.Default.Circle,
//                    contentDescription = null,
//                    tint = Color.Gray
//                )
//                Text(text = "InProgress Tasks (${projectSummery.inProgressTasks})")
//                Icon(
//                    imageVector = Icons.Default.Circle,
//                    contentDescription = null,
//                    tint = Color.Red
//                )
//                Text(text = "Late Tasks (${projectSummery.createdTasks - projectSummery.completedTasks - projectSummery.inProgressTasks})")
//            }
        }
    }
}

@Composable
fun NewProjectCard(
    team: TeamView,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    OutlinedCenteredCard(
        modifier = modifier,
        onClick = { navHostController.navigate(Screens.ProjectForm.withArgs(team.id, " ")) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text(text = "New Project", style = MaterialTheme.typography.headlineMedium)
        }
    }
}