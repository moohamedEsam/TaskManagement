package com.example.taskmanagement.presentation.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.composables.MemberComposable
import com.example.taskmanagement.presentation.navigation.Screens

@Composable
fun ProjectMembersPage(viewModel: ProjectViewModel, navHostController: NavHostController) {
    val project by viewModel.project
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(project.data?.members?.map { it.user } ?: emptyList()) {
            MemberComposable(
                user = it,
                modifier = Modifier.clickable {
                    navHostController.navigate(
                        Screens.Profile.withArgs(it.id)
                    )
                }) {

            }
        }
    }
}