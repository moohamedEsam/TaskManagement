package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CommentView(
    val description: String = "",
    val owner: String = "",
    var issuer: User = User(""),
    @Serializable(with = DateSerializer::class)
    val createdAt: Date = Date(),
    val id: String = UUID.randomUUID().toString()
) {
    fun toComment() = Comment(
        owner = owner,
        description = description,
        issuer = issuer.id,
        createdAt = createdAt,
        id = id
    )
}
