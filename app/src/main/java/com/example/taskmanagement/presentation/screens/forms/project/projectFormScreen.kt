package com.example.taskmanagement.presentation.screens.forms.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun ProjectFormScreen(
    snackbarHostState: SnackbarHostState,
    teamId: String,
    projectId: String
) {
    val viewModel: ProjectFormViewModel by inject { parametersOf(teamId, projectId) }
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    ProjectFormScreenContent(viewModel)
}

@Composable
fun ProjectFormScreenContent(
    viewModel: ProjectFormViewModel
) {
    val project by viewModel.projectView
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProjectHeader(project, viewModel)
        ProjectMembersList(viewModel)
        ProjectFooter(viewModel)
    }

}

@Composable
private fun ProjectHeader(
    project: ProjectView,
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
        ProjectOwnerTextField(project, viewModel)
        TeamPicker(viewModel)
    }
}

@Composable
private fun ProjectOwnerTextField(project: ProjectView, viewModel: ProjectFormViewModel) {
    val showField = viewModel.isUpdating && viewModel.hasPermission(Permission.EditOwner)
    if (!showField) return
    val team by viewModel.team
    OwnerTextField(textFieldValue = project.owner.username) { onDismiss ->
        MembersSuggestionsDialog(
            suggestions = (team.data?.members?.map { it.user } ?: emptyList()) - project.owner,
            onDismiss = onDismiss,
            onSearchChanged = {},
            onUserSelected = { viewModel.setProjectOwner(it) }
        )
    }
}

@Composable
fun TeamPicker(viewModel: ProjectFormViewModel) {
    val team by viewModel.team
    val showTeamDialog by viewModel.showTeamDialog
    PickerController(
        value = showTeamDialog,
        title = team.data?.name ?: "team",
        onToggle = { viewModel.toggleTeamDialog() }
    ) {
        TeamDialog(viewModel = viewModel) {
            viewModel.toggleTeamDialog()
        }
    }

}

@Composable
fun TeamDialog(viewModel: ProjectFormViewModel, onDismiss: () -> Unit) {
    val teams by viewModel.teams
    LaunchedEffect(key1 = teams) {
        if (teams is Resource.Initialized)
            viewModel.setTeams()
    }
    if (teams is Resource.Error || teams.data == null)
        return

    CardDialog(onDismiss = onDismiss) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
        ) {
            items(teams.data!!) {
                TeamPickerDialogCard(viewModel = viewModel, team = it, onDismiss = onDismiss)
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.ProjectMembersList(viewModel: ProjectFormViewModel) {
    val members = viewModel.members
    val team by viewModel.team

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.weight(0.8f)
    ) {
        item {
            Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
        }
        items(team.data?.members?.map { it.user } ?: emptyList()) { user ->
            MemberComposable(user = user) {
                Checkbox(
                    checked = members.contains(user),
                    onCheckedChange = { viewModel.toggleProjectMember(user) }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.ProjectFooter(
    viewModel: ProjectFormViewModel
) {

    Button(
        onClick = {
            viewModel.saveProject()
        },
        modifier = Modifier.align(Alignment.End)
    ) {
        Text(text = if (viewModel.isUpdating) "Update" else "Save")
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
            .fillMaxWidth()
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