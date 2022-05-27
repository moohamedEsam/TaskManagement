package com.example.taskmanagement.presentation.custom_components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.taskmanagement.domain.data_models.utils.ValidationResult

@Composable
fun TextFieldSetup(
    value: String,
    label: String,
    validationResult: ValidationResult,
    leadingIcon: @Composable (() -> Unit)?,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.animateContentSize()) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            isError = !validationResult.success,
            leadingIcon = leadingIcon
        )
        if (!validationResult.message.isNullOrBlank())
            Text(text = validationResult.message, color = MaterialTheme.colorScheme.error)
    }
}
