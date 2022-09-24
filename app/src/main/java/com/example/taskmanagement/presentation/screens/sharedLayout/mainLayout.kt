package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.navigation.Navigation
import com.example.taskmanagement.presentation.navigation.Screens
import kotlinx.coroutines.launch
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutine = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
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
                            containerColor = MaterialTheme.colorScheme.inversePrimary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                )
            },
            topBar = {
                if (!notAllowedScreens.contains(currentDestination?.destination?.route)) {
                    TopAppBarSetup(user = user, navHostController = navHostController) {
                        coroutine.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                }
            }
        )

    }
}

@Composable
private fun TopAppBarSetup(
    user: User,
    navHostController: NavHostController,
    onMenuClick: () -> Unit = {}
) {
    SmallTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(user.username)
            }
        },
        actions = {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
            UserIcon(
                photoPath = user.photoPath,
                modifier = Modifier.clickable { navHostController.navigate(Screens.Profile.route) }
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