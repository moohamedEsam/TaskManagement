package com.example.taskmanagement.presentation.screens.forms.task

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.customComponents.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainTaskFromPage(
    viewModel: TaskFormViewModel
) {
    val task by viewModel.taskView
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskFormHeader(task, viewModel)
        TaskFormFooter(viewModel = viewModel)
        Button(
            onClick = { viewModel.saveTask() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun TaskFormHeader(
    task: TaskView,
    viewModel: TaskFormViewModel
) {
    TextFieldSetup(
        value = task.title,
        label = "Title",
        validationResult = ValidationResult(true),
        leadingIcon = null,
        onValueChange = { viewModel.setTaskTitle(it) },
        enabled = viewModel.hasPermission(Permission.EditName)
    )

    TextFieldSetup(
        value = task.description ?: "",
        label = "Description",
        validationResult = ValidationResult(true),
        leadingIcon = null,
        onValueChange = { viewModel.setTaskDescription(it) },
        enabled = viewModel.hasPermission(Permission.EditName)
    )
    TaskOwnerTextField(viewModel = viewModel, task = task)
    ProjectPicker(viewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneTextField(task: TaskView, viewModel: TaskFormViewModel) {
    if (task.isMilestone)
        TextField(
            value = task.milestoneTitle,
            onValueChange = { viewModel.setTaskMilestoneTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Milestone Title") }
        )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.toggleIsMileStone(!task.isMilestone) }) {
        Checkbox(checked = task.isMilestone, onCheckedChange = { viewModel.toggleIsMileStone(it) })
        Text(text = "set task as milestone")
    }

}

@Composable
fun TaskOwnerTextField(viewModel: TaskFormViewModel, task: TaskView) {
    val showField = viewModel.isUpdating && viewModel.hasPermission(Permission.EditOwner)
    val project by viewModel.project
    if (!showField) return
    OwnerTextField(textFieldValue = task.owner.username) { onDismiss ->
        MembersSuggestionsDialog(
            suggestions = project.data?.members?.map { it.user } ?: emptyList(),
            onDismiss = onDismiss,
            onSearchChanged = {},
            onUserSelected = { viewModel.setOwner(it) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun TaskFormFooter(
    viewModel: TaskFormViewModel
) {
    val task by viewModel.taskView
    TaskDatePicker(task, viewModel)
    /*TextFieldSetup(
        value = task.estimatedTime?.toString() ?: "",
        label = "Estimated Time",
        validationResult = ValidationResult(true),
        leadingIcon = null,
        onValueChange = {
            if (it.all { value -> value.isDigit() } && it.isNotEmpty())
                viewModel.setTaskEstimatedTime(it.toInt())
        }
    )*/
    MilestoneTextField(task = task, viewModel = viewModel)
    PriorityList(task, viewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityList(
    task: TaskView,
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

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun TaskDatePicker(
    task: TaskView,
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
private fun ProjectPicker(viewModel: TaskFormViewModel) {
    val project by viewModel.project
    var showDialog by remember {
        mutableStateOf(false)
    }
    TextField(
        value = project.data?.name ?: "",
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        label = { Text(text = "Project") }
    )
    if (showDialog)
        ProjectDialog(viewModel) {
            showDialog = false
        }
}

@Composable
private fun ProjectDialog(
    viewModel: TaskFormViewModel,
    onDismissDialog: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getProjects()
    }
    val projects by viewModel.projects
    CardDialog(onDismiss = onDismissDialog) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(projects.data ?: emptyList()) {
                ProjectPickerDialogCard(
                    viewModel = viewModel,
                    project = it,
                    onDismissDialog = onDismissDialog
                )
            }
        }
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