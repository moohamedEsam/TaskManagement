package com.example.taskmanagement.presentation.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object SignUp : Screens("signUp")
    object Home : Screens("home")
    object Teams : Screens("teams")
    object Projects : Screens("projects")
    object Profile : Screens("profile")
    object Task : Screens("task")
    object TaskList : Screens("taskList")

}