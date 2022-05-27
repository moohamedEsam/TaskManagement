package com.example.taskmanagement.presentation.screens.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.screens.shared_layout.SharedLogin

@Composable
fun LoginScreenWrapper(navHostController: NavHostController) {
    SharedLogin {
        LoginScreen(navHostController)
    }
}