package com.example.taskmanagement.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.taskmanagement.presentation.composables.TaskItem
import org.koin.androidx.compose.get

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = get()
    val tasks by viewModel.tasks
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks.data ?: emptyList()) {
            TaskItem(task = it, onCompleteClick = {})
        }
    }
}