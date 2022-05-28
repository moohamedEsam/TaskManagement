package com.example.taskmanagement.presentation.screens.shared_layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagement.presentation.navigation.Navigation
import com.example.taskmanagement.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(startDestination: Screens) {
    val navHostController = rememberNavController()
    Scaffold(
        content = {
            Navigation(navHostController, startDestination)
            it
        },
        bottomBar = {
            //BottomBarSetup()
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
            Row {
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Search, null) })
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Search, null) })
                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Search, null) })
            }
        }

    }

}
