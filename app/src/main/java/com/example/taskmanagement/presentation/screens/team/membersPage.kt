package com.example.taskmanagement.presentation.screens.team

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.navigation.Screens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamMemberPage(team: TeamView, viewModel: TeamViewModel, navHostController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Text(
                text = "Members",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        items(items = team.members.map { it.user }, key = { it.id }) {
            MemberComposable(user = it, modifier = Modifier
                .animateItemPlacement()
                .clickable {
                    navHostController.navigate(
                        Screens.Profile.withArgs(it.id)
                    )
                }) {
                Spacer(modifier = Modifier.weight(0.8f))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.clickable { }
                )
            }
        }
        stickyHeader {
            Text(
                text = "Pending",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        items(items = team.pendingMembers, key = { it.id }) {
            MemberComposable(user = it, modifier = Modifier.animateItemPlacement()) {
                Spacer(modifier = Modifier.weight(0.8f))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }

}