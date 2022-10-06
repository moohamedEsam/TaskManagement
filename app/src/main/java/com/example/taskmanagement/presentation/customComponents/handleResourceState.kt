package com.example.taskmanagement.presentation.customComponents

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent


suspend fun handleSnackBarEvent(
    snackBarEvent: SnackBarEvent,
    snackbarHostState: SnackbarHostState
) {
    val result = snackbarHostState.showSnackbar(snackBarEvent.message, snackBarEvent.actionTitle)
    if (result == SnackbarResult.ActionPerformed)
        snackBarEvent.action()
}