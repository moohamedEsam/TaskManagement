package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.TaskStatus
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onCompleteClick: (Boolean) -> Unit,
    onclick: () -> Unit
) {
    var completed by remember {
        mutableStateOf(task.status == TaskStatus.Completed)
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onclick() },
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                CircleCheckbox(selected = completed) {
                    completed = !completed
                    onCompleteClick(completed)
                }
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = task.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TaskDate(task, task.finishDate)
                        Text(
                            text = task.priority.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = when (task.priority) {
                                Priority.High, Priority.Urgent -> Color.Red
                                Priority.Medium -> Color.Yellow
                                Priority.Low -> Color.Green
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun TaskDate(
    task: Task,
    finishDate: Date?
) {
    if (task.finishDate != null) {
        SubcomposeAsyncImage(
            model = R.drawable.date,
            contentDescription = null,
            imageLoader = get(),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = SimpleDateFormat.getDateInstance().format(task.finishDate),
            style = MaterialTheme.typography.bodySmall,
            color = if (task.status == TaskStatus.Completed && finishDate?.before(Date()) == true)
                Color.Red
            else
                MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}
