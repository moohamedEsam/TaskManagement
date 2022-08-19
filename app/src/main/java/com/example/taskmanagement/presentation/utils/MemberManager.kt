package com.example.taskmanagement.presentation.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.taskmanagement.domain.dataModels.user.User

interface MemberManager<T> {
    suspend fun toggleMember(value: User, members: SnapshotStateList<String>, view: MutableState<T>)
    suspend fun addMember(value: User, members: SnapshotStateList<String>, view: MutableState<T>)
    suspend fun removetUser(value: User, members: SnapshotStateList<String>, view: MutableState<T>)
}