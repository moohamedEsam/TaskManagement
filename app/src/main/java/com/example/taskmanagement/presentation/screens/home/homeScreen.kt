package com.example.taskmanagement.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.presentation.composables.TaskItem
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.get

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val viewModel: HomeViewModel = get()
    val tasks by viewModel.tasks
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    LaunchedEffect(key1 = tasks) {
        if (tasks is Resource.Error) {
            val result = snackbarHostState.showSnackbar(
                message = tasks.message ?: "something went wrong",
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.getUserTasks()
            }
        }
    }
    Box {
        LazyHorizontalGrid(
            rows = GridCells.Adaptive(90.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(tasks.data ?: emptyList()) {
                TaskItem(task = it, onCompleteClick = {}){
                    navHostController.navigate("${Screens.Task.route}/${it.id}")
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 64.dp, horizontal = 16.dp)
        )

    }
}