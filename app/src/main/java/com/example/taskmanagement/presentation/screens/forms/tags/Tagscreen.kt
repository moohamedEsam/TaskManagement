package com.example.taskmanagement.presentation.screens.forms.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.presentation.composables.TagComposable
import com.example.taskmanagement.presentation.customComponents.PermissionItem
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.godaddy.android.colorpicker.ClassicColorPicker
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun TagScreen(
    owner: String,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: TagViewModel by inject { parametersOf(owner) }
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    TagScreenContent(viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreenContent(viewModel: TagViewModel) {
    val tag by viewModel.tag
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = tag.title,
            onValueChange = { viewModel.setTitle(it) },
            label = { Text(text = "Title") },
            modifier = Modifier.fillMaxSize()
        )
        LazyColumn(
            modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 3).dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(Permission.values().toList() - Permission.EditTaskItems) {
                PermissionItem(
                    permission = it,
                    selected = tag.permissions.contains(it),
                    canEditPermission = true
                ) { selected ->
                    if (selected)
                        viewModel.addPermission(it)
                    else
                        viewModel.removePermission(it)

                }
            }
        }
        ClassicColorPicker(
            color = tag.getColor(),
            onColorChanged = { color ->
                color.toColor().run {
                    viewModel.setColor(listOf(red, green, blue))
                }
            },
            modifier = Modifier.height(200.dp),
            showAlphaBar = false
        )
        Text(text = "Preview", style = MaterialTheme.typography.headlineMedium)
        TagComposable(tag, Modifier.align(Alignment.CenterHorizontally))

        Button(onClick = { viewModel.saveTag() }, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Save")
        }
    }
}

