package com.example.taskmanagement.presentation.screens.forms.team

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Permission
import com.example.taskmanagement.domain.dataModels.utils.CreateTeamBody
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.customComponents.AddTeamMember
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TeamFormScreen(
    navHostController: NavHostController,
    teamId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TeamFormViewModel by inject { parametersOf(teamId) }
    val team by viewModel.requestBody
    TeamFormScreenContent(viewModel, team, snackbarHostState)

}

@Composable
fun TeamFormScreenContent(
    viewModel: TeamFormViewModel,
    team: CreateTeamBody,
    snackbarHostState: SnackbarHostState
) {
    val members = viewModel.members
    val isUpdating by viewModel.isUpdating
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TeamFormScreenHeader(team, viewModel)
        MembersList(members, viewModel)
        Button(
            onClick = {
                viewModel.saveTeam(snackbarHostState)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (isUpdating) "Update Team" else "Create Team")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MembersList(
    users: SnapshotStateMap<String, List<Permission>>,
    viewModel: TeamFormViewModel
) {
    val showDialog by viewModel.showDialog
    val validationResult by viewModel.usernameValidationResult
    val showDialogWithUser by viewModel.showDialogWithUser
    Column {
        Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item {
                IconButton(onClick = { viewModel.toggleDialog() }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
            items(items = users.keys.toList()) {
                SuggestionChip(
                    onClick = { viewModel.toggleDialog(users.toTeamUser(it)) },
                    label = { Text(text = it) }
                )
            }
        }
    }
    if (showDialog)
        AddTeamMember(
            initialValue = showDialogWithUser,
            onDismiss = { viewModel.toggleDialog() },
            validationResult = validationResult,
            onRemove = {
                viewModel.removeMember()
                viewModel.toggleDialog()
            }
        ) {
            viewModel.addMember(it)
        }
}

@Composable
private fun TeamFormScreenHeader(
    team: CreateTeamBody,
    viewModel: TeamFormViewModel
) {
    TextFieldSetup(
        value = team.name,
        label = "Title",
        validationResult = ValidationResult(true),
        leadingIcon = null,
        onValueChange = { viewModel.setName(it) }
    )

    OwnerTextField(viewModel, team)

    TextFieldSetup(
        value = team.description ?: "",
        label = "Description",
        validationResult = ValidationResult(true),
        leadingIcon = null,
        onValueChange = { viewModel.setDescription(it) }
    )
}

@Composable
fun OwnerTextField(viewModel: TeamFormViewModel, team: CreateTeamBody) {
    var currentRole by remember {
        mutableStateOf(viewModel.getCurrentUserRole())
    }
    val isUpdating by viewModel.isUpdating
    LaunchedEffect(key1 = isUpdating) {
        currentRole = viewModel.getCurrentUserRole()
    }
    if (!isUpdating) return
    TextField(
        value = team.owner,
        onValueChange = { viewModel.setOwner(it) },
        label = { Text(text = "Owner") },
        modifier = Modifier.fillMaxWidth(),
        enabled = (isUpdating && currentRole.permissions.any { it == Permission.EditOwner || it == Permission.FullControl }) || !isUpdating
    )
}
