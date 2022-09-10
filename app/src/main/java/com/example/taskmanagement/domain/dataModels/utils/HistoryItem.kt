package com.example.taskmanagement.domain.dataModels.utils

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class HistoryItem(
    val title: String,
    val id: String,
    val owner: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
)
