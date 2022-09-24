package com.example.taskmanagement.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.example.taskmanagement.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MembersListHeader(
    onFilter: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClose: () -> Unit
) {
    var progress by remember {
        mutableStateOf(0f)
    }
    val composable = rememberCoroutineScope()
    val context = LocalContext.current
    val scene =
        context.resources.openRawResource(R.raw.members_list_header_motion_layout).readBytes()
            .decodeToString()
    MotionLayout(motionScene = MotionScene(scene), progress = progress) {
        SearchMembersTextField(
            onFilter = onFilter,
            onSearch = onSearch,
            onClose = {
                composable.launch {
                    while (progress > 0.1f) {
                        progress -= 0.1f
                        delay(10)
                    }
                }
                onClose()
            },
            modifier = Modifier.layoutId("searchTextField")
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("header")
        ) {
            Text(text = "Members", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = {
                composable.launch {
                    while (progress < 1f) {
                        progress += 0.1f
                        delay(10)
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }

    }
}

@Composable
private fun SearchMembersTextField(
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    onClose: () -> Unit,
    onFilter: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var query by remember {
        mutableStateOf(initialQuery)
    }
    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onFilter(query)
        },
        label = { Text("Search Members") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onSearch(query)
                }
            )
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    onClose()
                }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}