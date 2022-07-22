package com.example.taskmanagement.presentation.customComponents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Permission

@Composable
fun MemberDialog(
    initialUser: ActiveUser,
    onDismiss: () -> Unit,
    onSave: (ActiveUser) -> Unit
) {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionItem(
    permission: Permission,
    value: Boolean,
    canEditPermission: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Checkbox(
            checked = value,
            onCheckedChange = {
                onClick()
            },
            enabled = canEditPermission
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = permission.toString())
    }
}