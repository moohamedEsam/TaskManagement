package com.example.taskmanagement.presentation.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.Project
import com.example.taskmanagement.domain.dataModels.Team
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.presentation.screens.team.TeamViewModel

@Composable
fun ProjectFormScreen(
    team: TeamView,
    viewModel: TeamViewModel,
    onDismiss: () -> Unit
) {
    ProjectFormScreenContent(team, viewModel, onDismiss)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormScreenContent(
    team: TeamView,
    viewModel: TeamViewModel,
    onDismiss: () -> Unit
) {
    val project by viewModel.project
    val members = viewModel.members
    Column(modifier = Modifier.padding(8.dp)) {
        ProjectHeader(project, viewModel)

        Text(
            text = "Members",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LazyRow(contentPadding = PaddingValues(8.dp)) {
            items(team.members) { member ->
                FilterChip(
                    selected = members[member.publicId] ?: false,
                    onClick = { viewModel.addProjectMember(member.publicId) },
                    label = { Text(text = member.username) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        ProjectFooter(viewModel, onDismiss)

    }
}

@Composable
private fun ProjectFooter(viewModel: TeamViewModel, onDismiss: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.8f))
        OutlinedButton(onClick = { viewModel.resetProject() }) {
            Text(text = "Reset")
        }
        Button(
            onClick = {
                viewModel.saveProject()
                onDismiss()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun ProjectHeader(
    project: Project,
    viewModel: TeamViewModel
) {
    Column {
        TextField(
            value = project.name,
            label = { Text(text = "Project Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onValueChange = { viewModel.setProjectName(it) },
        )

        TextField(
            value = project.description,
            label = { Text(text = "Project Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onValueChange = { viewModel.setProjectDescription(it) },
        )

    }
}
