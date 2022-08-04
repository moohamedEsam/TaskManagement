package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.customComponents.UserIcon

@Composable
fun MemberComposable(
    user: User,
    action: @Composable () -> Unit,
    onClick: (User) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(user) }
    ) {
        UserIcon(photoPath = user.photoPath)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = user.username, style = MaterialTheme.typography.headlineMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.weight(0.8f))
        action()
    }
}