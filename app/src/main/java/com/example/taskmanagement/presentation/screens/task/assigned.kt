package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.TaskScreenUIEvent
import com.example.taskmanagement.presentation.composables.MemberComposable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskAssignedPage(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    val task by viewModel.task.collectAsState()
    val taskView = task.data ?: return
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(taskView.assigned, key = { it.user.id }) {
            MemberComposable(user = it.user, modifier = Modifier.animateItemPlacement()) {
                Spacer(modifier = Modifier.weight(0.8f))
                IconButton(onClick = { viewModel.addEventUI(TaskScreenUIEvent.MembersRemove(it)) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }

}