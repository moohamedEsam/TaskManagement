package com.example.taskmanagement.domain.data_models

import java.util.*

data class Task(
    val title: String,
    val description: String,
    val owner: User,
    val assigned: List<User>,
    val status: TaskStatus,
    val finishDate: Date?,
    val completeDate: Date?,
    val estimatedTime: Int?,
)

