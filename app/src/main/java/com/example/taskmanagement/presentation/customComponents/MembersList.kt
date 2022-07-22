package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser

@Composable
fun MembersList(
    users: List<ActiveUser>,
    isSelected: (String) -> Boolean,
    onClick: (ActiveUser) -> Unit
) {

}

