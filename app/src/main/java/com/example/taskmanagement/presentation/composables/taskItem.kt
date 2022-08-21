package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onCompleteClick: (Boolean) -> Unit,
    onclick: () -> Unit
) {
    var completed by remember {
        mutableStateOf(task.status == TaskStatus.Completed)
    }
    Card(
        modifier = modifier,
        onClick = onclick
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.primary
                    )
                    CircleCheckbox(selected = completed) {
                        completed = !completed
                        onCompleteClick(completed)
                    }
                }
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                TaskDate(task, task.finishDate)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Priority: ")
                    Text(
                        text = task.priority.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (task.priority) {
                            Priority.High, Priority.Urgent -> Color.Red
                            Priority.Medium -> Color.Yellow
                            Priority.Low -> Color.Green
                        }
                    )

                }
                Text(text = "Completed Task Items ${task.taskItems.count { it.completed }}")
                Text(text = "Incomplete Task Items ${task.taskItems.count { !it.completed }}")
                Text(text = "Members: ${task.assigned.size}")

            }
        }
    }
}

@Composable
private fun TaskDate(
    task: Task,
    finishDate: Date?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (task.finishDate != null) {
            SubcomposeAsyncImage(
                model = R.drawable.date,
                contentDescription = null,
                imageLoader = get(),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = SimpleDateFormat.getDateInstance().format(task.finishDate),
                style = MaterialTheme.typography.bodyMedium,
                color = if (task.status == TaskStatus.Completed && finishDate?.before(Date()) == true)
                    Color.Red
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
