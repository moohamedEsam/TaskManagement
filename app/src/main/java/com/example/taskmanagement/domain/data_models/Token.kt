package com.example.taskmanagement.domain.data_models

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
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