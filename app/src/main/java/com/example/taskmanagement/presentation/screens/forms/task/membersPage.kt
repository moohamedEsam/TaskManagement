package com.example.taskmanagement.presentation.screens.forms.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.presentation.composables.MemberComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignedList(
    viewModel: TaskFormViewModel
) {
    val project by viewModel.project
    val assigned = viewModel.assigned
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { Text(text = "Assignees", style = MaterialTheme.typography.headlineMedium) }
        items(project.data?.members ?: emptyList()) {
            MemberComposable(user = it.user) {
                Spacer(modifier = Modifier.weight(0.8f))
                Checkbox(
                    checked = assigned.contains(it.user.id),
                    onCheckedChange = { _ -> viewModel.toggleTaskAssigned(it.user.id) })
            }
        }
    }
}
