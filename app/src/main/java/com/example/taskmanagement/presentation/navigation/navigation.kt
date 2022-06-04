package com.example.taskmanagement.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanagement.presentation.screens.home.HomeScreen
import com.example.taskmanagement.presentation.screens.login.LoginScreenWrapper
import com.example.taskmanagement.presentation.screens.profile.ProfileScreen
import com.example.taskmanagement.presentation.screens.project.ProjectScreen
import com.example.taskmanagement.presentation.screens.projects.ProjectsScreen
import com.example.taskmanagement.presentation.screens.signUp.SignUpScreen
import com.example.taskmanagement.presentation.screens.task.TaskScreen
import com.example.taskmanagement.presentation.screens.taskForm.TaskFormScreen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(
    navHostController: NavHostController,
    startDestination: Screens,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(Screens.SignIn.route) {
            LoginScreenWrapper(navHostController, snackbarHostState)
        }

        composable(Screens.SignUp.route) {
            SignUpScreen(navHostController = navHostController, snackbarHostState)
        }
        composable(Screens.Home.route) {
            HomeScreen(navHostController, snackbarHostState)
        }
        composable("${Screens.Task.route}/{taskId}") {
            val id = it.arguments?.getString("taskId")
            if (id != null) {
                TaskScreen(navHostController, id, snackbarHostState)
            }
        }

        composable(Screens.Projects.route) {
            ProjectsScreen(
                navHostController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }

        composable("${Screens.Project.route}/{projectId}") {
            val id = it.arguments?.getString("projectId")
            if (id != null) {
                ProjectScreen(
                    navHostController = navHostController,
                    snackbarHostState = snackbarHostState,
                    projectId = id
                )
            }
        }

        composable(Screens.Teams.route) {

        }

        composable(Screens.Profile.route) {
            ProfileScreen(
                navHostController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }

    }
}