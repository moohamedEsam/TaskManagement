package com.example.taskmanagement.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskManagementWithMongoDB.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.home.HomeScreen

@Composable
fun Navigation(navHostController: NavHostController, startDestination: Screens) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(Screens.Login.route) {

        }

        composable(Screens.SignUp.route) {

        }
        composable(Screens.Home.route) {
            HomeScreen()
        }
    }
}