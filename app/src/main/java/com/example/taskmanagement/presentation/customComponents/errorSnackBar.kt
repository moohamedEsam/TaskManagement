package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.utils.Resource


@Composable
fun ErrorSnackBar(
    errorMessage: String,
    actionText: String,
    modifier: Modifier,
    action: () -> Unit
) {
    val snackbarHostState = SnackbarHostState()
    LaunchedEffect(key1 = errorMessage) {
        val result = snackbarHostState.showSnackbar(errorMessage, actionText)
        if (result == SnackbarResult.ActionPerformed)
            action()
    }
    SnackbarHost(hostState = snackbarHostState, modifier = modifier)
}

@Composable
fun BoxScope.ResourceErrorSnackBar(
    resource: Resource<*>,
    actionText: String = "Retry",
    action: () -> Unit
) {
    if (resource is Resource.Error)
        ErrorSnackBar(
            errorMessage = resource.message ?: "",
            actionText = actionText,
            modifier = Modifier
                .padding(vertical = 64.dp)
                .align(Alignment.BottomStart),
            action = action
        )
}