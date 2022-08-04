package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.navigation.Navigation
import com.example.taskmanagement.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(startDestination: Screens, user: User?) {
    val navHostController = rememberNavController()
    val currentDestination by navHostController.currentBackStackEntryAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val notAllowedScreens = listOf(
        Screens.SignUp.route,
        Screens.SignIn.route
    )
    Scaffold(
        content = {
            Navigation(navHostController, startDestination, snackBarHostState, it)
        },
        bottomBar = {
            if (!notAllowedScreens.contains(currentDestination?.destination?.route)) {
                BottomBarSetup(navHostController)
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            user?.let {
                if (!notAllowedScreens.contains(currentDestination?.destination?.route)) {
                    TopAppBarSetup(user = it, navHostController = navHostController)
                }
            }
        }
    )
}

@Composable
private fun TopAppBarSetup(user: User, navHostController: NavHostController) {
    SmallTopAppBar(
        title = { Text(user.username) },
        actions = {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
            UserIcon(
                photoPath = user.photoPath
            )
        }
    )
}

@Composable
fun BottomBarSetup(navHostController: NavHostController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        BottomNavItem(navHostController, Screens.Home, R.drawable.task)
        BottomNavItem(navHostController, Screens.Projects, R.drawable.project)
        FormNavigatorButton(navHostController)
        BottomNavItem(navHostController, Screens.Teams, R.drawable.team)
        BottomNavItem(navHostController, Screens.Profile, R.drawable.profile)
    }


}

@Composable
private fun FormNavigatorButton(navHostController: NavHostController) {
    val currentDestination by navHostController.currentBackStackEntryAsState()
    FloatingActionButton(
        onClick = {
            when (currentDestination?.destination?.route) {
                Screens.Home.route -> {
                    navHostController.navigate(Screens.TaskForm.withArgs("  "))
                }
                Screens.Projects.route -> {
                    navHostController.navigate(Screens.ProjectForm.withArgs("   "))
                }
                Screens.Teams.route -> {
                    navHostController.navigate(Screens.TeamForm.withArgs("  "))
                }
            }
        },
        modifier = Modifier.size(48.dp)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Composable
private fun BottomNavItem(
    navHostController: NavHostController,
    screen: Screens,
    @DrawableRes icon: Int
) {
    val currentDestination by navHostController.currentBackStackEntryAsState()
    NavigationRailItem(
        selected = currentDestination?.destination?.route == screen.route,
        onClick = {
            navHostController.navigate(screen.route) {
                launchSingleTop = true
                popUpTo(navHostController.backQueue[1].destination.route ?: screen.route) {
                    inclusive = true
                }
            }
        },
        icon = {
            Icon(
                painterResource(id = icon),
                null,
            )
        },
    )
}