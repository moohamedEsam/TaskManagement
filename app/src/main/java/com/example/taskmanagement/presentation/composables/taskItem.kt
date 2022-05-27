package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.taskmanagement.presentation.custom_components.CircleCheckbox
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.data_models.TaskDetails
import com.example.taskmanagement.domain.data_models.TaskStatus
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: TaskDetails,
    onCompleteClick: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                CircleCheckbox(selected = task.status == TaskStatus.Completed) {}
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SubcomposeAsyncImage(
                            model = R.drawable.date,
                            contentDescription = null,
                            imageLoader = get(),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        if (task.finishDate != null)
                            Text(
                                text = SimpleDateFormat.getDateInstance().format(task.finishDate),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (task.status == TaskStatus.Completed && task.finishDate.before(Date()))
                                    Color.Red
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )


                    }
                }
            }
        }
    }
}
