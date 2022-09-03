package com.example.taskmanagement.presentation.screens.forms.task

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemsList(viewModel: TaskFormViewModel) {
    val taskItems by viewModel.taskItems.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                NewTaskItemTextField(viewModel) {
                    viewModel.addTaskItem(it)
                }
            }
        }
        items(taskItems.toList()) {
            TaskItemCard(taskItem = it, viewModel = viewModel)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskItemCard(taskItem: TaskItem, viewModel: TaskFormViewModel) {
    Card {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = taskItem.title, style = MaterialTheme.typography.headlineSmall)
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    modifier = Modifier.clickable { viewModel.removeTaskItem(taskItem) })
            }

        }
    }
}

@Composable
fun NewTaskItemTextField(viewModel: TaskFormViewModel, onSave: (TaskItem) -> Unit) {
    var value by remember {
        mutableStateOf("")
    }
    val validationResult by viewModel.taskItemValidationResult.collectAsState()
    Column(modifier = Modifier
        .padding(8.dp)
        .animateContentSize()) {
        TextField(
            value = value,
            onValueChange = {
                viewModel.validateTaskItemTitle(it)
                value = it
            },
            label = { Text(text = "task item title") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        if (!validationResult.isValid)
                            return@clickable
                        onSave(TaskItem(value))
                        value = ""
                    }
                )
            },
            isError = !validationResult.isValid,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (validationResult.isValid)
                        onSave(TaskItem((value)))
                }
            ),
            singleLine = true
        )
        if (!validationResult.isValid)
            Text(text = validationResult.message ?: "", color = MaterialTheme.colorScheme.error)

    }
}
