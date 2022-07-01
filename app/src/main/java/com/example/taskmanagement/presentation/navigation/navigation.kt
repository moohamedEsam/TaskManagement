package com.example.taskmanagement.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskmanagement.presentation.screens.forms.project.ProjectFormScreen
import com.example.taskmanagement.presentation.screens.forms.task.TaskFormScreen
import com.example.taskmanagement.presentation.screens.forms.team.TeamFormScreen
import com.example.taskmanagement.presentation.screens.home.HomeScreen
import com.example.taskmanagement.presentation.screens.login.LoginScreenWrapper
import com.example.taskmanagement.presentation.screens.profile.ProfileScreen
import com.example.taskmanagement.presentation.screens.project.ProjectScreen
import com.example.taskmanagement.presentation.screens.projects.ProjectsScreen
import com.example.taskmanagement.presentation.screens.signUp.SignUpScreen
import com.example.taskmanagement.presentation.screens.task.TaskScreen
import com.example.taskmanagement.presentation.screens.team.TeamScreen

@Composable
fun Navigation(
    navHostController: NavHostController,
    startDestination: Screens,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination.route,
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
    ) {
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
            TeamScreen(
                navHostController = navHostController,
                teamId = "d94da931-ed1d-41d3-b55d-3aa51793f298",
                snackbarHostState = snackbarHostState
            )
        }

        composable(Screens.Profile.route) {
            ProfileScreen(
                navHostController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }

        composable(route = "${Screens.TaskForm.route}/{projectId}") {
            val projectId = it.arguments?.getString("projectId", "  ") ?: "  "
            TaskFormScreen(
                snackbarHostState = snackbarHostState,
                projectId = projectId
            )
        }
        composable("${Screens.ProjectForm.route}/{teamId}") {
            val teamId = it.arguments?.getString("teamId", "  ") ?: "  "
            ProjectFormScreen(snackbarHostState = snackbarHostState, teamId = teamId)
        }

        composable("${Screens.TeamForm.route}/{teamId}") {
            val teamId = it.arguments?.getString("teamId", "    ") ?: "   "
            TeamFormScreen(
                navHostController = navHostController,
                teamId = teamId,
                snackbarHostState = snackbarHostState
            )
        }

    }
}