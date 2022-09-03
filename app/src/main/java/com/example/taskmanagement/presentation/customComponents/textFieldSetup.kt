package com.example.taskmanagement.presentation.customComponents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TextFieldSetup(
    value: String,
    label: String,
    validationResult: StateFlow<ValidationResult>,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    val validation by validationResult.collectAsState()
    Column(modifier = Modifier.animateContentSize()) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(text = label) },
            modifier = modifier.fillMaxWidth(),
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = !validation.isValid,
            leadingIcon = leadingIcon,
            enabled = enabled
        )
        if (!validation.message.isNullOrBlank())
            Text(text = validation.message!!, color = MaterialTheme.colorScheme.error)
    }
}
