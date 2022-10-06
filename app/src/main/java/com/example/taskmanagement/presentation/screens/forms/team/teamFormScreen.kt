package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.composables.MembersListHeader
import com.example.taskmanagement.presentation.composables.SuggestionsDropDownMenu
import com.example.taskmanagement.presentation.customComponents.OwnerTextField
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    TeamFormScreenContent(viewModel, navHostController, snackbarHostState)
}

@Composable
fun TeamFormScreenContent(
    viewModel: TeamFormViewModel,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
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
        MembersList(viewModel, modifier = Modifier.weight(1f), snackbarHostState)
        SaveButton(
            viewModel = viewModel,
            isUpdating = isUpdating,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun SaveButton(
    viewModel: TeamFormViewModel,
    isUpdating: Boolean,
    modifier: Modifier = Modifier
) {
    var showButton by remember {
        mutableStateOf(true)
    }
    val canSave by viewModel.canSave.collectAsState()
    if (showButton)
        Button(
            onClick = {
                viewModel.saveTeam(onLoading = { showButton = false }) { showButton = true }
            },
            modifier = modifier,
            enabled = canSave
        ) {
            Text(
                text = if (isUpdating) "Update Team" else "Create Team",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    else
        CircularProgressIndicator(modifier = modifier.scale(0.5f))
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
    viewModel: TeamFormViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    val isUpdating = viewModel.isUpdating
    val team by viewModel.teamView.collectAsState()
    val members by viewModel.members.collectAsState()
    val suggestions by viewModel.membersSuggestions.collectAsState()
    var showDialog by remember {
        mutableStateOf(false)
    }
    val coroutine = rememberCoroutineScope()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures {
                showDialog = false
            }
        }
    ) {
        item {
            Column {
                MembersListHeader(
                    onFilter = {
                        viewModel.searchMembers(it)
                        showDialog = true
                    },
                    onClose = { showDialog = false },
                    onSearch = { showDialog = true }
                )
                SuggestionsDropDownMenu(showDialog, suggestions, members) {
                    if (!isUpdating)
                        viewModel.toggleMember(it)
                    else
                        coroutine.launch {
                            snackbarHostState.showSnackbar("New Members can't be added while updating")
                        }
                }
            }
        }
        if (!showDialog)
            items(if (isUpdating) team.members.map { it.user } else members.toList()) { user ->
                MemberComposable(user = user) {
                    Spacer(modifier = Modifier.weight(0.8f))
                    if (isUpdating)
                        Checkbox(
                            checked = members.contains(user),
                            onCheckedChange = { viewModel.toggleMember(user) },
                            enabled = viewModel.hasPermission(Permission.EditMembers)
                        )
                    else
                        IconButton(onClick = { viewModel.toggleMember(user) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                }
            }
    }

}

@Composable
private fun TeamOwnerTextField(viewModel: TeamFormViewModel, team: TeamView) {
    val showField = viewModel.isUpdating && viewModel.hasPermission(Permission.EditOwner)
    if (!showField) return
    val members by viewModel.members.collectAsState()
    OwnerTextField(textFieldValue = team.owner.username, members) {
        viewModel.setOwner(it)
    }
}