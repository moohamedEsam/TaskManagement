package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.customComponents.fillMaxHeight
import com.example.taskmanagement.presentation.navigation.Navigation
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.login.MainLayoutViewModel
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
        ExpandableColumn(title = "Teams") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dashboard.teams, key = { it.id }) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navHostController.navigate(
                                    Screens.Team.withArgs(it.id)
                                )
                            },
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        ExpandableColumn(title = "Projects") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dashboard.projects, key = { it.id }) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navHostController.navigate(
                                    Screens.Project.withArgs(it.id)
                                )
                            },
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        ExpandableColumn(title = "Tasks") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dashboard.tasks, key = { it.id }) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navHostController.navigate(
                                    Screens.Task.withArgs(it.id)
                                )
                            },
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandableColumn(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            if (!expanded)
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.Black
                )
            else
                Icon(
                    imageVector = Icons.Default.ExpandLess,
                    contentDescription = "Expand",
                    tint = Color.Black
                )
        }
        if (expanded)
            content()

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