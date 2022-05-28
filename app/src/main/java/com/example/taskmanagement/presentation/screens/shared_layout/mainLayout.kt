package com.example.taskmanagement.presentation.screens.shared_layout

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
                BottomBarSetup()
            }
        }
    )
}

@Composable
fun BottomBarSetup() {

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .padding(bottom = 16.dp)
                .align(CenterHorizontally),
        ) {
            Row(modifier=Modifier.padding(horizontal = 16.dp)) {
                NavigationRailItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.task),
                            null,
                        )
                    },
                )
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(painterResource(id = R.drawable.project), null) })
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(painterResource(id = R.drawable.team), null) })
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(painterResource(id = R.drawable.profile), null) })
            }
        }

    }

}
