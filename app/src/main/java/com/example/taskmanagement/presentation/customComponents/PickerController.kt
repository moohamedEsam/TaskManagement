package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PickerController(
    value: Boolean,
    title: String,
    toggleValue: () -> Unit,
    contentToShow: @Composable () -> Unit
) {
    TextField(
        value = title,
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                toggleValue()
            }
    )
    if (value) {
        contentToShow()
    }
}

@Composable
fun <T> PickerDialog(
    items: List<T>,
    item: @Composable (T) -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = onDismissDialog) {
        Surface {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .padding(8.dp)
            ) {
                items(items) {
                    item(it)
                }
            }
        }
    }
}