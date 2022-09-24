package com.example.taskmanagement.presentation.screens.task

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.task.CommentView
import com.example.taskmanagement.domain.dataModels.task.TaskScreenUIEvent
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.composables.MemberComposable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCommentsPage(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    val comments by viewModel.comments.collectAsState()
    val task by viewModel.task.collectAsState()
    val users by remember {
        derivedStateOf {
            task.data?.assigned?.map { it }?.plus(task.data!!.owner)?.toSet() ?: emptySet()
        }
    }
    val updateAllowed by viewModel.isUpdateAllowed.collectAsState()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        items(comments.toList(), key = { it.id }) {
            CommentCardItem(
                comment = it,
                users = users,
                viewModel = viewModel,
                modifier = Modifier.animateItemPlacement(),
                showActions = updateAllowed && viewModel.currentUser.id == it.issuer.id
            )
        }

        if (updateAllowed)
            item {
                NewCommentCardItem(
                    viewModel = viewModel,
                    users = users,
                    modifier = Modifier.animateItemPlacement()
                )
            }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentCardItem(
    comment: CommentView,
    users: Set<User>,
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    showActions: Boolean = false,

    ) {
    var showOwner by remember {
        mutableStateOf(false)
    }
    OutlinedCard(modifier = modifier, onClick = { showOwner = !showOwner }) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize()
        ) {
            Text(
                text = getCommentAnnotatedString(users = users, text = comment.description),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .verticalScroll(rememberScrollState())
            )
            if (showActions)
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(
                        onClick = { viewModel.addEventUI(TaskScreenUIEvent.Comments.Remove(comment)) }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            if (showOwner)
                Text(
                    text = buildAnnotatedString {
                        append("By ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)) {
                            append(comment.issuer.username)
                        }
                    }
                )
        }
    }
}

@Composable
private fun NewCommentCardItem(
    viewModel: TaskViewModel,
    users: Set<User>,
    modifier: Modifier = Modifier
) {
    var showSuggestions by remember {
        mutableStateOf(false)
    }
    var value by remember {
        mutableStateOf(TextFieldValue())
    }
    val onSave = onSave@{
        if (value.text.isBlank() || value.text.length < 3) return@onSave
        val comment = CommentView(description = value.text)
        viewModel.addEventUI(TaskScreenUIEvent.Comments.Add(comment))
        value = TextFieldValue()
    }
    Column(modifier = modifier.animateContentSize()) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                showSuggestions = it.text.isNotEmpty() && it.text.last() == '@'
            },
            label = { Text(text = "New Comment") },
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSave()
                }
            ),
            trailingIcon = {
                IconButton(onClick = onSave) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                }
            },

            )
        Box(modifier = Modifier.fillMaxWidth()) {
            DropdownMenu(
                expanded = showSuggestions,
                onDismissRequest = { showSuggestions = false },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(0.8f)
            ) {
                users.forEach {
                    DropdownMenuItem(
                        text = {
                            MemberComposable(user = it) {}
                        },
                        onClick = {
                            val text = "${value.text}${it.username} "
                            value = TextFieldValue(text, TextRange(text.length))
                            showSuggestions = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun getCommentAnnotatedString(
    users: Set<User>,
    text: String
): AnnotatedString {
    val tokens = text.split(" ").filter { it.isNotBlank() }
    val usernames = users.map { it.username }
    return buildAnnotatedString {
        for (word in tokens) {
            if (word[0] == '@' && usernames.contains(word.drop(1))) {
                withStyle(SpanStyle(color = Color.Green)) {
                    append("$word ")
                }
            } else
                append("$word ")
        }
    }
}