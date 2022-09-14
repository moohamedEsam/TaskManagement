package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskPie(
    createdTasks: Int,
    completedTasks: Int,
    inProgressTasks: Int,
    modifier: Modifier = Modifier
) {
    val lateTasks = createdTasks - completedTasks - inProgressTasks

    Column(modifier = modifier) {
        Text(
            text = "Task Overall",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            if (createdTasks == 0)
                DrawDonat(
                    Color.Black to 1,
                    modifier = Modifier.size(maxWidth, maxHeight.div(1.9f))
                )
            else
                DrawDonat(
                    Color.Red to lateTasks,
                    Color.Yellow to inProgressTasks,
                    Color.Green to completedTasks,
                    modifier = Modifier.size(maxWidth, maxHeight.div(1.9f))
                )
            Text(text = "$createdTasks")
        }
        if (createdTasks != 0) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Text(
                    text = "Completed ${completedTasks * 100 / createdTasks}%",
                    color = Color.Green,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "InProgress ${inProgressTasks * 100 / createdTasks}%",
                    color = Color.Yellow,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "OverDue ${lateTasks * 100 / createdTasks}%",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}