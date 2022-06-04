package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class Token(
    val token: String,
    val expiresIn: Long,
    @Transient
    val expired: Boolean = Date(expiresIn).before(Date())
) 