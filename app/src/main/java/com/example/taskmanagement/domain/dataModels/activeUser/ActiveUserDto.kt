package com.example.taskmanagement.domain.dataModels.activeUser

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable

@Serializable
data class ActiveUserDto(
    val user: User,
    val tag: Tag?
) {
    fun toActiveUser() = ActiveUser(user.id, tag?.id)
}
