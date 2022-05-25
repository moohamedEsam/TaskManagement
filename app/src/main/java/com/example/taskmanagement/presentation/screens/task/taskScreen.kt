package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.data_models.Task
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskScreen(
    navHostController: NavHostController,
    taskId: UUID?,
    task: Task
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = task.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                if (task.finishDate != null) {
                    Text(text = "Date", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = SimpleDateFormat.getDateInstance().format(task.finishDate),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.TopCenter)
                    .width(1.dp)
            )



            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                if (task.finishDate != null){
                    Text(text = "Time", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = SimpleDateFormat.getDateInstance().format(task.finishDate),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        Divider()
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Description", style = MaterialTheme.typography.bodyLarge)
        Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "sub tasks", style = MaterialTheme.typography.bodyLarge)

    }
}