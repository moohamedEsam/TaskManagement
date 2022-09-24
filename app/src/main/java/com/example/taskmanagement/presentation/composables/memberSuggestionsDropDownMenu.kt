package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsDropDownMenu(
    showDialog: Boolean,
    suggestions: Resource<List<User>>,
    members: Collection<User>,
    onClick: (User) -> Unit
) {
    if (!showDialog) return
    val users = suggestions.data ?: emptyList()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (suggestions is Resource.Loading)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        items(users) { user ->
            MemberComposable(user = user) {
                Spacer(modifier = Modifier.weight(0.8f))
                Checkbox(
                    checked = members.contains(user),
                    onCheckedChange = { onClick(user) }
                )
            }
        }
    }
}