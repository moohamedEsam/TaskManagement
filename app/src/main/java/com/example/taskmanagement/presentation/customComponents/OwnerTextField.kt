package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun OwnerTextField(textFieldValue: String, dialog: @Composable (() -> Unit) -> Unit) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    TextField(
        value = textFieldValue,
        label = { Text("Owner") },
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        enabled = false
    )
    if (showDialog)
        dialog {
            showDialog = false
        }
}