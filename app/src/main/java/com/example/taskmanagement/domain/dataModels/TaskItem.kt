package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class TaskItem(val title: String, val completed: Boolean)
