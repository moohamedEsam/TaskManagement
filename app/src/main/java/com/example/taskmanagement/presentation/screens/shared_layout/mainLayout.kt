package com.example.taskmanagement.presentation.screens.shared_layout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagement.R
import com.example.taskmanagement.presentation.navigation.Navigation
import com.example.taskmanagement.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(startDestination: Screens) {
    val navHostController = rememberNavController()
    val currentDestination by navHostController.currentBackStackEntryAsState()
    Scaffold(
        content = {
            Navigation(navHostController, startDestination)
            it
        },
        bottomBar = {
            if (!listOf(
                    Screens.SignUp.route,
                    Screens.SignIn.route
                ).contains(currentDestination?.destination?.route)
            ) {
                BottomBarSetup(navHostController)
            }
        }
    )
}

@Composable
fun BottomBarSetup(navHostController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .padding(bottom = 16.dp)
                .align(CenterHorizontally),
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                BottomNavItem(navHostController, Screens.Home, R.drawable.task)
                BottomNavItem(navHostController, Screens.Projects, R.drawable.project)
                BottomNavItem(navHostController, Screens.Teams, R.drawable.team)
                BottomNavItem(navHostController, Screens.Profile, R.drawable.profile)
            }
        }

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
                popUpTo(screen.route) {
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
