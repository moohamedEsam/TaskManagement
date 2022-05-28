package com.example.taskmanagement.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanagement.presentation.screens.home.HomeScreen
import com.example.taskmanagement.presentation.screens.login.LoginScreenWrapper
import com.example.taskmanagement.presentation.screens.signUp.SignUpScreen

@Composable
fun Navigation(navHostController: NavHostController, startDestination: Screens) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(Screens.SignIn.route) {
            LoginScreenWrapper(navHostController)
        }

        composable(Screens.SignUp.route) {
            SignUpScreen(navHostController = navHostController)
        }
        composable(Screens.Home.route) {
            HomeScreen()
        }
        composable(Screens.Task.route) {

        }

        composable(Screens.TaskList.route) {

        }
    }
}