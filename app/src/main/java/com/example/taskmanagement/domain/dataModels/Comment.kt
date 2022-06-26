package com.example.taskmanagement.domain.dataModels

import com.example.taskmanagement.domain.utils.custom_serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Comment(
    val owner: String,
    val description: String,
    val mentions: List<String>?,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
)
