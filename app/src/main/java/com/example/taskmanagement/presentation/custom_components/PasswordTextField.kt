package com.example.taskmanagement.presentation.custom_components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.taskmanagement.domain.data_models.utils.ValidationResult


@Composable
fun PasswordTextField(
    value: String,
    label: String = "Password",
    validationResult: ValidationResult,
    onValueChange: (String) -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.animateContentSize()) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = if (showPassword)
                        Icons.Default.VisibilityOff
                    else
                        Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable { showPassword = !showPassword }
                )
            },
            visualTransformation = if (showPassword)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            maxLines = 1,
            isError = !validationResult.isValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (!validationResult.message.isNullOrBlank())
            Text(text = validationResult.message, color = MaterialTheme.colorScheme.error)
    }

}