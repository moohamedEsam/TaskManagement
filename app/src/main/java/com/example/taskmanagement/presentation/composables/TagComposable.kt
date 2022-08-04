package com.example.taskmanagement.presentation.composables

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanagement.domain.dataModels.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagComposable(tag: Tag, modifier: Modifier = Modifier) {
    SuggestionChip(
        onClick = { },
        label = { Text(text = tag.title, style = MaterialTheme.typography.titleMedium) },
        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = tag.getColor()),
        modifier = modifier
    )
}