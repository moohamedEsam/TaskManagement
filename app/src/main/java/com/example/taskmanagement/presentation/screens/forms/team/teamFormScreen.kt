package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.customComponents.UserIcon
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TeamFormScreen(
    navHostController: NavHostController,
    teamId: String,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TeamFormViewModel by inject { parametersOf(teamId) }
    TeamFormScreenContent(viewModel, snackbarHostState)
}

@Composable
fun TeamFormScreenContent(
    viewModel: TeamFormViewModel,
    snackbarHostState: SnackbarHostState
) {
    val team by viewModel.team
    val isUpdating by viewModel.isUpdating
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
                viewModel.saveTeam(snackbarHostState)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (isUpdating) "Update Team" else "Create Team")
        }
    }
}

@Composable
private fun TeamFormScreenHeader(
    team: Team,
    viewModel: TeamFormViewModel
) {
    TextFieldSetup(
        value = team.name,
        label = "Title",
        validationResult = ValidationResult(true),
        enabled = viewModel.hasPermission(Permission.EditName),
        leadingIcon = null,
        onValueChange = { viewModel.setName(it) }
    )

    OwnerTextField(viewModel, team)

    TextFieldSetup(
        value = team.description ?: "",
        label = "Description",
        validationResult = ValidationResult(true),
        enabled = viewModel.hasPermission(Permission.EditName),
        leadingIcon = null,
        onValueChange = { viewModel.setDescription(it) }
    )
}

@Composable
private fun MembersList(
    viewModel: TeamFormViewModel
) {
    val members = viewModel.members
    var showSearchMemberDialog by remember {
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
                IconButton(onClick = { showSearchMemberDialog = true }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        }
        items(members) {
            MemberComposable(user = it, { }) {}
        }
    }

    if (showSearchMemberDialog)
        SearchMemberTextField(viewModel = viewModel) {
            showSearchMemberDialog = false
        }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchMemberTextField(viewModel: TeamFormViewModel, onDismiss: () -> Unit) {
    var query by remember {
        mutableStateOf("")
    }
    val suggestions by viewModel.memberSuggestions
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxSize(0.8f)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (query.isNotBlank() && query.length > 2)
                        viewModel.searchMembers(query)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text(text = "Search Members") }
            )

            MemberSuggestionsMenu(suggestions) {
                viewModel.addMember(it)
            }

        }

    }
}

@Composable
private fun MemberSuggestionsMenu(
    suggestions: List<User>,
    onClick: (User) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(suggestions) {
            MemberComposable(
                user = it,
                action = {
                    IconButton(onClick = { onClick(it) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                },
                onClick = { }
            )
        }
    }
}

@Composable
private fun OwnerTextField(viewModel: TeamFormViewModel, team: Team) {
    val isUpdating by viewModel.isUpdating
    if (!isUpdating) return
    TextFieldSetup(
        value = team.owner,
        label = "Owner",
        enabled = viewModel.hasPermission(Permission.EditOwner),
        leadingIcon = null,
        onValueChange = { viewModel.setOwner(it) },
        validationResult = ValidationResult(true)
    )
}