package com.example.taskmanagement.presentation.customComponents

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.taskmanagement.domain.dataModels.utils.Resource


@Composable
fun HandleResourceChange(
    resource: Resource<*>,
    onSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onSnackbarClick: () -> Unit,
    actionLabel: String? = "Retry"
) {
    LaunchedEffect(key1 = resource) {
        when (resource) {
            is Resource.Success -> onSuccess()
            is Resource.Error -> {
                val result = snackbarHostState.showSnackbar(resource.message ?: "", actionLabel)
                if (result == SnackbarResult.ActionPerformed)
                    onSnackbarClick()

            }
            else -> Unit
        }
    }
}