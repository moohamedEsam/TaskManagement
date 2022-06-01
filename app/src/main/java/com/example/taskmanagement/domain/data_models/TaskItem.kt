package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.Serializable

@Serializable
data class TaskItem(val title: String, val completed: Boolean)
