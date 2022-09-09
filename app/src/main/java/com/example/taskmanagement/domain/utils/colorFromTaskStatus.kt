package com.example.taskmanagement.domain.utils

import androidx.compose.ui.graphics.Color
import com.example.taskmanagement.domain.dataModels.task.TaskStatus

fun Color.Companion.fromStatus(status: TaskStatus) = when (status) {
    TaskStatus.Completed -> Green
    TaskStatus.Pending -> Gray
    TaskStatus.Late -> Red
    TaskStatus.Canceled -> Gray
    TaskStatus.InProgress -> Yellow
}
