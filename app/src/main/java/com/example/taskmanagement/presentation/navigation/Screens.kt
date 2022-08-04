package com.example.taskmanagement.presentation.navigation

sealed class Screens(val route: String) {
    object SignIn : Screens("login")
    object SignUp : Screens("signUp")
    object Home : Screens("home")
    object Teams : Screens("teams")
    object Team : Screens("team")
    object Projects : Screens("projects")
    object Project : Screens("project")
    object Profile : Screens("profile")
    object Task : Screens("task")
    object TaskList : Screens("taskList")
    object TaskForm : Screens("taskForm")
    object ProjectForm : Screens("projectForm")
    object TeamForm : Screens("teamForm")
    object TagForm : Screens("tagForm")

    fun withArgs(vararg path: String) = buildString {
        append(route)
        path.forEach {
            append("/$it")
        }
    }

}