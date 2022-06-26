package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
enum class Permission {
    View,
    Create,
    EditName,
    EditMembers,
    EditMembersPermission,
    EditOwner,
    EditTaskItems,
    Delete,
    Share,
    Download,
    FullControl
}