package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.customComponents.ElevatedCenteredCard
import com.example.taskmanagement.presentation.customComponents.OutlinedCenteredCard
import com.example.taskmanagement.presentation.customComponents.fillMaxHeight
import com.example.taskmanagement.presentation.customComponents.fillMaxWidth
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.projects.ProjectItem

private const val ratio = 5

@Composable
fun TasksPage(navHostController: NavHostController, viewModel: ProjectViewModel) {
    val project by viewModel.project
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            OutlinedCenteredCard(
                modifier = Modifier.fillMaxHeight(ratio),
                onClick = {
                    navHostController.navigate(
                        Screens.TaskForm.withArgs(
                            project.data?.id ?: "", " "
                        )
                    )
                }
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(text = "New Task")
                }
            }
        }
        items(project.data?.tasks ?: emptyList()) { task ->
            ElevatedCenteredCard(
                modifier = Modifier.fillMaxHeight(ratio),
                onClick = {
                    navHostController.navigate(
                        Screens.TaskForm.withArgs(
                            project.data?.id ?: "", task.id
                        )
                    )
                }
            ) {
                Text(text = task.title, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}