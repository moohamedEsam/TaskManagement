package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionItem(
    permission: Permission,
    selected: Boolean,
    canEditPermission: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(!selected)
            }
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = {
                onClick(it)
            },
            enabled = canEditPermission
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = permission.toString())
    }
}