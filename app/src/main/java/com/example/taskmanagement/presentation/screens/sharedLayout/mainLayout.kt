package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.taskmanagement.presentation.screens.login.MainLayoutViewModel
import org.koin.androidx.compose.inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(startDestination: Screens) {
    val navHostController = rememberNavController()
    val currentDestination by navHostController.currentBackStackEntryAsState()
    val viewModel: MainLayoutViewModel by inject()
    val userResource by viewModel.userProfile.collectAsState()
    val user by remember {
        derivedStateOf {
            userResource.data ?: User("")
        }
    }
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val notAllowedScreens = listOf(
        Screens.SignUp.route,
        Screens.SignIn.route
    )
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                viewModel = viewModel,
                user = user,
                navHostController = navHostController
            )
        }) {
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
                SnackbarHost(
                    hostState = snackBarHostState,
                    snackbar = {
                        Snackbar(
                            it,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                )
            },
            topBar = {
                if (!notAllowedScreens.contains(currentDestination?.destination?.route)) {
                    TopAppBarSetup(user = user, navHostController = navHostController)
                }
            }
        )

    }
}

@Composable
private fun DrawerContent(
    viewModel: MainLayoutViewModel,
    user: User,
    navHostController: NavHostController
) {
    val dashboardResource by viewModel.userDashboard.collectAsState()
    val dashboard = dashboardResource.data ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserIcon(
                modifier = Modifier.size(100.dp),
                photoPath = user.photoPath
            )
            Text(text = user.username)
            Text(text = user.email)
        }
        Text("Teams")
        Text(text = "Projects")
        Text(text = "Tasks")
    }
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
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
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
        BottomNavItem(navHostController, Screens.Teams, R.drawable.team)
        BottomNavItem(navHostController, Screens.Profile, R.drawable.profile)
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