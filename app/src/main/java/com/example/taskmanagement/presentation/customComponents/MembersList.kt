package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.User

@Composable
fun MembersList(users: List<User>, navHostController: NavHostController) {
    Column {
        Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(users) {
                UserItem(user = it, navHostController = navHostController)
            }
        }
    }
}

@Composable
fun UserItem(user: User, navHostController: NavHostController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UserIcon(
            user = user,
            navHostController = navHostController,
            modifier = Modifier.size(48.dp)
        )
        Text(text = user.username)
    }
}