package com.example.taskmanagement.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.utils.HistoryItem
import java.text.SimpleDateFormat

@Composable
fun TaskHistoryPage(viewModel: TaskViewModel) {
    val task by viewModel.task.collectAsState()
    val historyItems by remember {
        derivedStateOf {
            task.data?.history ?: emptyList()
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        items(historyItems) { item ->
            TaskHistoryItem(item)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskHistoryItem(historyItem: HistoryItem) {
    val simpleDateFormat = remember {
        SimpleDateFormat("MMM dd, yyyy")
    }
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = historyItem.title,
                modifier = Modifier
                    .height(50.dp)
                    .verticalScroll(rememberScrollState())
            )
            Text(
                text = simpleDateFormat.format(historyItem.createdAt),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}