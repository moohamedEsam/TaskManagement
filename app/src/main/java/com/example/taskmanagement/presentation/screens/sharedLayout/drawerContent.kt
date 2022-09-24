package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun DrawerContent(
    viewModel: MainLayoutViewModel,
    user: User,
    navHostController: NavHostController,
) {
    val dashboardResource by viewModel.userDashboard.collectAsState()
    val currentDestination by navHostController.currentBackStackEntryAsState()
    val dashboard = dashboardResource.data ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MemberComposable(user = user, imageSize = 48.dp) {}
        ExpandableNavigationDrawerItem(
            title = "Teams",
            icon = { Icon(imageVector = Icons.Outlined.Group, contentDescription = null) },
            selected = currentDestination?.destination?.route == Screens.Teams.route,
        ) {
            ExpandableNavigationItemContent(
                values = dashboard.teams.map { it.name to Screens.Team.withArgs(it.id) },
                navHostController = navHostController
            )
        }
        ExpandableNavigationDrawerItem(
            title = "Projects",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.project),
                    contentDescription = null
                )
            },
            selected = currentDestination?.destination?.route == Screens.Projects.route,
        ) {
            ExpandableNavigationItemContent(
                values = dashboard.projects.map {
                    it.name to Screens.Project.withArgs(
                        it.id
                    )
                },
                navHostController = navHostController
            )
        }
        ExpandableNavigationDrawerItem(
            title = "Tasks",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.task),
                    contentDescription = null
                )
            },
            selected = currentDestination?.destination?.route == Screens.Home.route,
        ) {
            ExpandableNavigationItemContent(
                values = dashboard.tasks.map {
                    it.title to Screens.Task.withArgs(
                        it.id
                    )
                },
                navHostController = navHostController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandableNavigationDrawerItem(
    title: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val onClick = { expanded = !expanded }
    Column(
        modifier = Modifier
            .animateContentSize()
            .clickable { expanded = !expanded }
    ) {
        NavigationDrawerItem(
            label = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title)
                    if (!expanded)
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Expand"
                        )
                    else
                        Icon(
                            imageVector = Icons.Default.ExpandLess,
                            contentDescription = "Expand"
                        )
                }
            },
            selected = selected,
            onClick = onClick,
            icon = icon
        )

        if (expanded)
            content()

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandableNavigationItemContent(
    values: List<Pair<String, String>>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(values) {
            NavigationDrawerItem(
                label = { Text(text = it.first) },
                selected = false,
                onClick = { navHostController.navigate(it.second) }
            )
        }
    }
}