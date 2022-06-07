package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowDialogFormButton(
    label: String,
    modifier: Modifier = Modifier,
    dialogContent: @Composable (() -> Unit) -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    ExtendedFloatingActionButton(
        onClick = { showDialog = true },
        text = { Text(text = label) },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                null
            )
        }
    )
    if (showDialog)
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                dialogContent {
                    showDialog = false
                }
            }
        }
}