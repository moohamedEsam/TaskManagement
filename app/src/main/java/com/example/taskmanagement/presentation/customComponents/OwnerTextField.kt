package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerTextField(
    textFieldValue: String,
    members: Collection<User>,
    onValueChange: (User) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = {},
            readOnly = true,
            label = { Text("Owner") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            members.forEach { member ->
                DropdownMenuItem(
                    text = { Text(member.username) },
                    onClick = {
                        onValueChange(member)
                        expanded = false
                    },
                    leadingIcon = { UserIcon(photoPath = member.photoPath) }
                )
            }
        }
    }

}
