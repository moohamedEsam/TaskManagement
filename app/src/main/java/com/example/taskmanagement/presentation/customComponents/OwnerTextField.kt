package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable

@Composable
fun OwnerTextField(
    textFieldValue: String,
    members: Collection<User>,
    onValueChange: (User) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Column {
        TextField(
            value = textFieldValue,
            label = { Text("Owner") },
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            enabled = false
        )
        DropdownMenu(
            expanded = showDialog,
            onDismissRequest = { showDialog = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 200.dp),
        ) {
            members.forEach { member ->
                DropdownMenuItem(
                    text = { Text(text = member.username) },
                    leadingIcon = { UserIcon(photoPath = member.photoPath) },
                    onClick = {
                        onValueChange(member)
                        showDialog = false
                    }
                )
            }
        }
    }

}
