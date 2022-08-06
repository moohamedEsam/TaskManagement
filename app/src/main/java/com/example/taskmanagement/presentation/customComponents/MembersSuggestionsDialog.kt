package com.example.taskmanagement.presentation.customComponents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MembersSuggestionsDialog(
    suggestions: List<User>,
    onDismiss: () -> Unit,
    onSearchChanged: (String) -> Unit,
    onUserSelected: (User) -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxSize(0.8f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        onSearchChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text(text = "Search Members") }
                )

                MemberSuggestionsList(suggestions, onUserSelected)
            }
        }

    }
}

@Composable
fun MemberSuggestionsList(
    suggestions: List<User>,
    onClick: (User) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(suggestions) {
            MemberComposable(user = it) {
                IconButton(onClick = { onClick(it) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}