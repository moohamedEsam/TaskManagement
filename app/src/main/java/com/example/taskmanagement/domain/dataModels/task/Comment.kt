package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import com.example.taskmanagement.domain.utils.custom_serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Comment(
    val owner: String,
    val description: String,
    val mentions: List<String> = emptyList(),
    @Serializable(with = DateSerializer::class)
    val createdAt: Date = Date(),
    val id: String
)
