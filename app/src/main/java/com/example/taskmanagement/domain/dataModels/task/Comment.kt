package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Comment(
    val owner: String = UUID.randomUUID().toString(),
    val description: String = "",
    val mentions: List<String> = emptyList(),
    @Serializable(with = DateSerializer::class)
    val createdAt: Date = Date(),
    val id: String = UUID.randomUUID().toString()
)
