package com.example.taskmanagement.presentation.screens.task

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.task.TaskScreenUIEvent
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.presentation.customComponents.CircleCheckbox

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItemsPage(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    val taskItems by viewModel.taskItems.collectAsState()
    val updateAllowed by viewModel.isUpdateAllowed.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (updateAllowed)
            item {
                NewTaskItemCardItem(viewModel = viewModel) {
                    viewModel.addEventUI(TaskScreenUIEvent.TaskItems.Add(it))
                }
            }
        items(taskItems.toList(), key = { it.id }) {
            TaskItemCardItem(
                taskItem = it,
                viewModel = viewModel,
                modifier = Modifier.animateItemPlacement(),
                enabled = updateAllowed && (it.completedBy
                    ?: viewModel.currentUser.id) == viewModel.currentUser.id
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskItemCardItem(
    taskItem: TaskItem,
    viewModel: TaskViewModel,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(selected = taskItem.isCompleted, enabled = enabled) {
                viewModel.addEventUI(
                    TaskScreenUIEvent.TaskItems.Edit(
                        taskItem.copy(isCompleted = !taskItem.isCompleted),
                        taskItem
                    )
                )
            }

            Text(text = taskItem.title, modifier = Modifier.weight(0.8f))
            IconButton(onClick = { viewModel.addEventUI(TaskScreenUIEvent.TaskItems.Remove(taskItem)) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
private fun NewTaskItemCardItem(viewModel: TaskViewModel, onSave: (TaskItem) -> Unit) {
    var title by remember {
        mutableStateOf("")
    }
    val validationResult by viewModel.taskItemTitleValidationResult.collectAsState()
    val onSaveClick = click@{
        if (!validationResult.isValid) return@click
        onSave(TaskItem(title))
        title = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                viewModel.validateTaskItemTitle(it)
            },
            label = { Text(text = "New Task Item") },
            trailingIcon = {
                IconButton(
                    onClick = onSaveClick
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { onSaveClick() }
            ),
            isError = !validationResult.isValid
        )
        if (!validationResult.isValid)
            Text(text = validationResult.message ?: "", color = MaterialTheme.colorScheme.error)
    }

}