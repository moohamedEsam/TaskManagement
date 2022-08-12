package com.example.taskmanagement.presentation.screens.forms.task

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.presentation.customComponents.PickerController
import com.example.taskmanagement.presentation.customComponents.PickerDialog
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.screens.task.TaskItemCard
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskFormScreen(
    snackbarHostState: SnackbarHostState,
    projectId: String
) {
    val viewModel: TaskFormViewModel by inject { parametersOf(projectId) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        TaskFormScreenContent(viewModel = viewModel, snackbarHostState = snackbarHostState)
    }

}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TaskFormScreenContent(
    viewModel: TaskFormViewModel,
    snackbarHostState: SnackbarHostState
) {
    val task by viewModel.task
    val taskItems = viewModel.taskItems
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
            TaskFormFooter(snackbarHostState, viewModel)
        }

    }
}

@Composable
private fun AddNewTaskItem(viewModel: TaskFormViewModel) {
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

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun TaskFormFooter(
    snackbarHostState: SnackbarHostState,
    viewModel: TaskFormViewModel
) {
    val assigned = viewModel.assigned
    val project by viewModel.project
    val task by viewModel.task
    Column {
        AssignedList(project, assigned, viewModel)
        Spacer(modifier = Modifier.height(8.dp))
        TaskDatePicker(task, viewModel)
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
        PriorityList(task, viewModel)
        Spacer(modifier = Modifier.height(8.dp))
        ActionRow(viewModel, snackbarHostState)
    }
}

@Composable
private fun ColumnScope.ActionRow(
    viewModel: TaskFormViewModel,
    snackbarHostState: SnackbarHostState
) {
    Row(modifier = Modifier.Companion.align(Alignment.End)) {
        OutlinedButton(onClick = {
            viewModel.resetTask()
        }) {
            Text("Reset")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            viewModel.saveTask(snackbarHostState = snackbarHostState)
        }) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityList(
    task: Task,
    viewModel: TaskFormViewModel
) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssignedList(
    project: Resource<ProjectView>,
    assigned: SnapshotStateMap<String, Boolean>,
    viewModel: TaskFormViewModel
) {
//    Spacer(modifier = Modifier.height(8.dp))
//    Text(text = "Assignees", style = MaterialTheme.typography.headlineMedium)
//    Spacer(modifier = Modifier.height(4.dp))
//    LazyRow {
//        items(project.data?.members ?: emptyList()) {
//            FilterChip(
//                selected = assigned[it.user] ?: false,
//                onClick = {
//                    viewModel.addTaskAssigned(it.user)
//                },
//                label = { Text(it.user) },
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun TaskDatePicker(
    task: Task,
    viewModel: TaskFormViewModel
) {
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
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun TaskFormHeader(
    task: Task,
    viewModel: TaskFormViewModel
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
        ProjectPicker(viewModel)
        Text("Task Items", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun ProjectPicker(viewModel: TaskFormViewModel) {
    val project by viewModel.project
    var showDialog by remember {
        mutableStateOf(false)
    }
    PickerController(
        value = showDialog,
        title = project.data?.name ?: "Project",
        onToggle = { showDialog = !showDialog }
    ) {
        ProjectDialog(viewModel = viewModel) {
            showDialog = false
        }
    }
}

@Composable
fun ProjectDialog(
    viewModel: TaskFormViewModel,
    onDismissDialog: () -> Unit
) {
    val projects by viewModel.projects
    projects.onSuccess {
        PickerDialog(
            items = it,
            item = { project ->
                ProjectPickerDialogCard(
                    viewModel = viewModel,
                    project = project,
                    onDismissDialog = onDismissDialog
                )
            },
            onDismissDialog = onDismissDialog
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectPickerDialogCard(
    viewModel: TaskFormViewModel,
    project: Project,
    onDismissDialog: () -> Unit
) {
    OutlinedCard(modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    viewModel.setProject(project.id)
                    onDismissDialog()
                }
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = project.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Members: ${project.members.count()}")
        }
    }
}
