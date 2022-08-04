package com.example.taskmanagement.presentation.screens.forms.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.customComponents.*
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun ProjectFormScreen(
    snackbarHostState: SnackbarHostState,
    teamId: String,
    projectId: String
) {
    val viewModel: ProjectFormViewModel by inject { parametersOf(teamId, projectId) }
    ProjectFormScreenContent(viewModel, snackbarHostState)
}

@Composable
fun ProjectFormScreenContent(
    viewModel: ProjectFormViewModel,
    snackbarHostState: SnackbarHostState
) {
    val project by viewModel.project
    val team by viewModel.team
    Column {
        ProjectHeader(project, viewModel)
        ProjectMembersList(viewModel)
        ProjectFooter(viewModel, snackbarHostState)
    }
    HandleResourceChange(
        resource = team,
        onSuccess = { },
        snackbarHostState = snackbarHostState,
        onSnackbarClick = { })
}

@Composable
fun ProjectMembersList(viewModel: ProjectFormViewModel) {
    val members = viewModel.members
    val teamMemberDialog by viewModel.showTeamMembersDialog
    val memberToAdd by viewModel.userToUpdate
    MembersList(
        users = members.map { it.value.first },
        isSelected = { userId ->
            members[userId]?.second ?: false
        },
        onClick = { activeUser -> viewModel.toggleTeamMemberDialog(activeUser) }
    )

    if (teamMemberDialog && memberToAdd != null)
        MemberDialog(
            initialUser = memberToAdd!!,
            onDismiss = { viewModel.toggleTeamMemberDialog() }
        ) {
            viewModel.addProjectMember(it)
        }
}

@Composable
private fun ColumnScope.ProjectFooter(
    viewModel: ProjectFormViewModel,
    snackbarHostState: SnackbarHostState
) {
    Row(modifier = Modifier.align(Alignment.End)) {
        OutlinedButton(onClick = { viewModel.resetProject() }) {
            Text(text = "Reset")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                viewModel.saveProject(snackbarHostState)
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun ProjectHeader(
    project: Project,
    viewModel: ProjectFormViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextFieldSetup(
            value = project.name,
            label = "Project Name",
            validationResult = ValidationResult(true),
            leadingIcon = null,
            onValueChange = { viewModel.setProjectName(it) },
            enabled = viewModel.hasPermission(Permission.EditName)
        )
        TextFieldSetup(
            value = project.description,
            label = "Project Description",
            onValueChange = { viewModel.setProjectDescription(it) },
            enabled = viewModel.hasPermission(Permission.EditName),
            leadingIcon = null,
            validationResult = ValidationResult(true)
        )
        OwnerTextField(viewModel)
        TeamPicker(viewModel)
    }
}

@Composable
private fun OwnerTextField(viewModel: ProjectFormViewModel) {
    val isUpdating by viewModel.isUpdating
    if (!isUpdating) return
    TextField(
        value = viewModel.getProjectOwner(),
        label = { Text("Owner") },
        onValueChange = { viewModel.setProjectOwner(it) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.toggleOwnerDialog() },
        enabled = false
    )

}

@Composable
fun TeamPicker(viewModel: ProjectFormViewModel) {
    val team by viewModel.team
    val showTeamDialog by viewModel.showTeamDialog
    PickerController(
        value = showTeamDialog,
        title = team.data?.name ?: "team",
        toggleValue = { viewModel.toggleTeamDialog() }
    ) {
        TeamDialog(viewModel = viewModel) {
            viewModel.toggleTeamDialog()
        }
    }

}

@Composable
fun TeamDialog(viewModel: ProjectFormViewModel, onDismiss: () -> Unit) {
    val teams by viewModel.teams
    teams.onSuccess {
        Dialog(onDismissRequest = onDismiss) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(it) {
                    TeamPickerDialogCard(viewModel = viewModel, team = it, onDismiss = onDismiss)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamPickerDialogCard(
    viewModel: ProjectFormViewModel,
    team: Team,
    onDismiss: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                viewModel.setTeam(team.id)
                onDismiss()
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = team.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = team.description ?: "", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Members: ${team.members.size}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}