package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavHostController
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.*
import kotlinx.coroutines.delay
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
        MembersList(viewModel, modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.saveTeam {
                    //navHostController.navigate(Screens.Team.withArgs(it))
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = if (isUpdating) "Update Team" else "Create Team",
                color = MaterialTheme.colorScheme.onPrimary
            )
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
    viewModel: TeamFormViewModel,
    modifier: Modifier = Modifier
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
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures {
                showDialog = false
            }
        }
    ) {
        item {
            Column {
                MembersListHeader(onFilter = {
                    viewModel.searchMembers(it)
                    showDialog = true
                }) {
                    showDialog = true
                }
                SuggestionsDropDownMenu(showDialog, suggestions, viewModel)
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

}

@Composable
private fun SuggestionsDropDownMenu(
    showDialog: Boolean,
    suggestions: Set<User>,
    viewModel: TeamFormViewModel
) {
    if (!showDialog) return
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (suggestions.isEmpty()) {
            item {
                Text(text = "No suggestions")
            }
            return@LazyColumn
        }
        items(suggestions.toList()) { user ->
            MemberComposable(user = user) {
                Spacer(modifier = Modifier.weight(0.8f))
                IconButton(onClick = { viewModel.addMember(user) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }

}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MembersListHeader(onFilter: (String) -> Unit, onSearch: (String) -> Unit) {
    var progress by remember {
        mutableStateOf(0f)
    }
    val composable = rememberCoroutineScope()
    val context = LocalContext.current
    val scene =
        context.resources.openRawResource(R.raw.members_list_header_motion_layout).readBytes()
            .decodeToString()
    MotionLayout(motionScene = MotionScene(scene), progress = progress) {
        SearchMembersTextField(
            onFilter = onFilter,
            onSearch = onSearch,
            onClose = {
                composable.launch {
                    while (progress > 0.1f) {
                        progress -= 0.1f
                        delay(10)
                    }
                }
            },
            modifier = Modifier.layoutId("searchTextField")
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("header")
        ) {
            Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = {
                composable.launch {
                    while (progress < 1f) {
                        progress += 0.1f
                        delay(10)
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }

    }
}

@Composable
private fun SearchMembersTextField(
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    onClose: () -> Unit,
    onFilter: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var query by remember {
        mutableStateOf(initialQuery)
    }
    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onFilter(query)
        },
        label = { Text("Search Members") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onSearch(query)
                }
            )
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    onClose()
                }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
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