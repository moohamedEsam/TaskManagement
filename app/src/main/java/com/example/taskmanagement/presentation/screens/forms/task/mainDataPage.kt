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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.presentation.customComponents.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainTaskFromPage(
    viewModel: TaskFormViewModel
) {
    val task by viewModel.taskView.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskFormHeader(task, viewModel)
        TaskFormFooter(viewModel = viewModel)
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
        validationResult = viewModel.taskTitleValidationResult,
        leadingIcon = null,
        onValueChange = { viewModel.setTaskTitle(it) },
        enabled = viewModel.hasPermission(Permission.EditName)
    )

    TextField(
        value = task.description ?: "",
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { viewModel.setTaskDescription(it) },
        enabled = viewModel.hasPermission(Permission.EditName)
    )
    ProjectPicker(viewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneTextField(task: TaskView, viewModel: TaskFormViewModel) {
    if (task.isMilestone)
        TextFieldSetup(
            value = task.milestoneTitle,
            validationResult = viewModel.taskMilestoneTitleValidationResult,
            onValueChange = { viewModel.setTaskMilestoneTitle(it) },
            label = "Milestone Title",
            enabled = viewModel.hasPermission(Permission.EditName)
        )
    val onClick: (Boolean) -> Unit = { value ->
        if (viewModel.hasPermission(Permission.EditName))
            viewModel.toggleIsMileStone(value)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(!task.isMilestone) }
    ) {
        Checkbox(checked = task.isMilestone, onCheckedChange = onClick)
        Text(text = "set task as milestone")
    }

}

@Composable
private fun TaskFormFooter(
    viewModel: TaskFormViewModel
) {
    val task by viewModel.taskView.collectAsState()
    val estimatedTime by viewModel.taskEstimatedTimeText.collectAsState()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        TaskDatePicker(task, viewModel)
    }
    TextFieldSetup(
        value = estimatedTime,
        label = "Estimated Time",
        validationResult = viewModel.taskEstimatedTimeValidationResult,
        leadingIcon = null,
        onValueChange = {
            viewModel.setTaskEstimatedTime(it)
        },
        keyboardType = KeyboardType.Number,
        enabled = viewModel.hasPermission(Permission.EditName)
    )
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
                modifier = Modifier.padding(8.dp),
                enabled = viewModel.hasPermission(Permission.EditName)
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
    TextFieldSetup(
        value = task.finishDate?.let { simpleDateFormat.format(it) } ?: "",
        label = "Finish date",
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!viewModel.hasPermission(Permission.EditName)) return@clickable
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
        },
        validationResult = viewModel.taskFinishDateValidationResult
    )
}


@Composable
private fun ProjectPicker(viewModel: TaskFormViewModel) {
    val project by viewModel.project.collectAsState()
    var showDialog by remember {
        mutableStateOf(false)
    }
    TextField(
        value = project.data?.name ?: "",
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (viewModel.hasPermission(Permission.EditName))
                    showDialog = true
            },
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
    val projects by viewModel.projects.collectAsState()
    CardDialog(onDismiss = onDismissDialog) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (projects.data?.isEmpty() == true){
                item {
                    Text(text = "No projects found")
                }
            }
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