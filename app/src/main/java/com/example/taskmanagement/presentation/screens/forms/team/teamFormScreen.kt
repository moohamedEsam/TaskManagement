package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.MembersSuggestionsDialog
import com.example.taskmanagement.presentation.customComponents.OwnerTextField
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TeamFormScreen(
    navHostController: NavHostController,
    teamId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TeamFormViewModel by inject { parametersOf(teamId) }
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    TeamFormScreenContent(viewModel, navHostController)
}

@Composable
fun TeamFormScreenContent(
    viewModel: TeamFormViewModel, navHostController: NavHostController
) {
    val team by viewModel.teamView.collectAsState()
    val isUpdating = viewModel.isUpdating
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TeamFormScreenHeader(team, viewModel)
        MembersList(viewModel)
        Button(
            onClick = {
                viewModel.saveTeam {
                    //navHostController.navigate(Screens.Team.withArgs(it))
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (isUpdating) "Update Team" else "Create Team")
        }
    }
}

@Composable
private fun TeamFormScreenHeader(
    team: TeamView,
    viewModel: TeamFormViewModel
) {
    TextFieldSetup(
        value = team.name,
        label = "Title",
        validationResult = viewModel.teamNameValidationResult,
        enabled = viewModel.hasPermission(Permission.EditName),
        leadingIcon = null,
        onValueChange = { viewModel.setName(it) }
    )

    TeamOwnerTextField(viewModel, team)

    TextField(
        value = team.description ?: "",
        label = { Text("Description") },
        enabled = viewModel.hasPermission(Permission.EditName),
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { viewModel.setDescription(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MembersList(
    viewModel: TeamFormViewModel
) {
    val isUpdating = viewModel.isUpdating
    val team by viewModel.teamView.collectAsState()
    val members by viewModel.members.collectAsState()
    val suggestions by viewModel.membersSuggestions.collectAsState()
    var showDialog by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 3).dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        }

        items(if (isUpdating) team.members.map { it.user } else members.toList()) { user ->
            MemberComposable(user = user) {
                Spacer(modifier = Modifier.weight(0.8f))
                if (isUpdating)
                    Checkbox(
                        checked = members.contains(user),
                        onCheckedChange = { viewModel.toggleMember(user) }
                    )
                else
                    IconButton(onClick = { viewModel.toggleMember(user) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
            }
        }
    }

    if (showDialog)
        MembersSuggestionsDialog(
            suggestions = suggestions,
            onDismiss = { showDialog = false },
            onSearchChanged = {
                if (it.isNotBlank() && it.length > 2)
                    viewModel.searchMembers(it)
            }
        ) {
            viewModel.addMember(it)
        }

}

@Composable
private fun TeamOwnerTextField(viewModel: TeamFormViewModel, team: TeamView) {
    val showField = viewModel.isUpdating && viewModel.hasPermission(Permission.EditOwner)
    if (!showField) return
    OwnerTextField(textFieldValue = team.owner.username) { onDismiss ->
        MembersSuggestionsDialog(
            suggestions = team.members.map { it.user }.toSet(),
            onDismiss = onDismiss,
            onSearchChanged = {}
        ) { viewModel.setOwner(it) }
    }
}