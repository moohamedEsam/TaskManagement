package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.taskmanagement.domain.dataModels.Permission
import com.example.taskmanagement.domain.dataModels.Role
import com.example.taskmanagement.domain.dataModels.utils.TeamUser
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTeamMember(
    initialValue: TeamUser?,
    validationResult: ValidationResult,
    canEditPermission: Boolean = true,
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
    onSave: (TeamUser) -> Unit
) {
    var user by remember {
        mutableStateOf(initialValue ?: TeamUser("", Role(emptyList())))
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(0.9f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                TextFieldSetup(
                    value = user.username,
                    label = "Username",
                    validationResult = validationResult,
                    leadingIcon = null,
                    onValueChange = { user = user.copy(username = it) }
                )
            }
            item {
                Text(text = "Permissions", style = MaterialTheme.typography.headlineMedium)
            }
            items(Permission.values()) { permission ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (canEditPermission || initialValue == null)
                                user = addRole(user, permission)
                        }
                ) {
                    Checkbox(
                        checked = user.role.permissions.contains(permission),
                        onCheckedChange = {
                            user = addRole(user, permission)
                        },
                        enabled = canEditPermission || initialValue == null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = permission.toString())
                }

            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (initialValue == null)
                        Spacer(modifier = Modifier.weight(0.8f))
                    else
                        OutlinedButton(onClick = { onRemove() }) {
                            Text(text = "Remove", color = MaterialTheme.colorScheme.error)
                        }
                    Button(
                        onClick = {
                            onSave(user)
                        },
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

private fun addRole(
    user: TeamUser,
    permission: Permission
): TeamUser {
    val roles = user.role.let {
        if (user.role.permissions.contains(permission))
            it.copy(permissions = it.permissions - permission)
        else
            it.copy(permissions = it.permissions + permission)
    }
    return user.copy(role = roles)
}