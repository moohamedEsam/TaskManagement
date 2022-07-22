package com.example.taskmanagement.domain.dataModels.task

import kotlinx.serialization.Serializable

@Serializable
enum class Permission {
    View,
    Create,
    EditName,
    EditMembers,
    EditOwner,
    EditTaskItems,
    Delete,
    Share,
    Download,
    FullControl
}