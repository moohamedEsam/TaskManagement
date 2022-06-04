package com.example.taskmanagement.presentation.screens.taskForm

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.screens.project.ProjectViewModel
import com.example.taskmanagement.presentation.screens.task.TaskItemCard
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskFormScreen(
    project: ProjectView,
    viewModel: ProjectViewModel
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        TaskFormScreenContent(project, viewModel)
    }

}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TaskFormScreenContent(
    project: ProjectView,
    viewModel: ProjectViewModel
) {
    val task by viewModel.task
    val taskItems = viewModel.taskItems
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.8f)
            .padding(8.dp)
    ) {
        item {
            TaskFormHeader(task, viewModel)
        }
        items(taskItems) { item ->
            TaskItemCard(item)
        }
        item {
            AddNewTaskItem(viewModel)
        }
        item {
            TaskFormFooter(project, task, viewModel)
        }

    }
}

@Composable
private fun AddNewTaskItem(viewModel: ProjectViewModel) {
    Column {
        var value by remember {
            mutableStateOf("")
        }
        TextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Add new item") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = {
                viewModel.addTaskItem(value)
                value = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun TaskFormFooter(
    project: ProjectView,
    task: Task,
    viewModel: ProjectViewModel
) {
    val assigned = viewModel.assigned
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Assignees", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow {
            items(project.members) {
                FilterChip(
                    selected = assigned[it.publicId] ?: false,
                    onClick = {
                        viewModel.addTaskAssigned(it.publicId)
                    },
                    label = { Text(it.username) },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val dateDialog = DatePickerDialog(LocalContext.current)
        val calendar = Calendar.getInstance()
        val simpleDateFormat =
            SimpleDateFormat.getDateTimeInstance(
                SimpleDateFormat.MEDIUM,
                SimpleDateFormat.SHORT
            )
        TextField(
            value = task.finishDate?.let { simpleDateFormat.format(it) } ?: "",
            label = { Text("Finish date") },
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dateDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        viewModel.setTaskFinishDate(calendar.time)
                    }
                    dateDialog.show()
                }
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextFieldSetup(
            value = task.estimatedTime?.toString() ?: "",
            label = "Estimated Time",
            validationResult = ValidationResult(true),
            leadingIcon = null,
            onValueChange = {
                if (it.all { value -> value.isDigit() } && it.isNotEmpty())
                    viewModel.setTaskEstimatedTime(it.toInt())
            }
        )
        Text(text = "Priority", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow {
            items(Priority.values()) {
                FilterChip(
                    selected = task.priority == it,
                    onClick = { viewModel.setTaskPriority(it) },
                    label = { Text(it.name) },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.align(Alignment.End)) {
            OutlinedButton(onClick = {
                viewModel.resetTask()
            }) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.saveTask()
            }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun TaskFormHeader(
    task: Task,
    viewModel: ProjectViewModel
) {
    Column {
        TextFieldSetup(
            value = task.title,
            label = "Title",
            validationResult = ValidationResult(true),
            leadingIcon = null,
            onValueChange = { viewModel.setTaskTitle(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextFieldSetup(
            value = task.description ?: "",
            label = "Description",
            validationResult = ValidationResult(true),
            leadingIcon = null,
            onValueChange = { viewModel.setTaskDescription(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Task Items", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
    }
}
