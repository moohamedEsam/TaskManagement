package com.example.taskmanagement.presentation.screens.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.screens.sharedLayout.SharedLogin

@Composable
fun LoginScreenWrapper(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    SharedLogin {
        LoginScreen(navHostController, snackbarHostState)
    }
}