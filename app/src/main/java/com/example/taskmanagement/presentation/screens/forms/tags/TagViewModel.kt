package com.example.taskmanagement.presentation.screens.forms.tags

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.TagScope
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch
import java.util.*

class TagViewModel(private val repository: MainRepository, owner: String) : ViewModel() {
    val tag =
        mutableStateOf(
            Tag(
                permissions = emptyList(),
                title = "",
                color = Color.Transparent.run {
                    listOf(red, green, blue)
                },
                scope = TagScope.Team,
                owner = owner,
                id = UUID.randomUUID().toString()
            )
        )

    fun setTitle(value: String) {
        tag.value = tag.value.copy(title = value)
    }

    fun addPermission(permission: Permission) {
        tag.value = tag.value.copy(permissions = tag.value.permissions + permission)
    }

    fun removePermission(permission: Permission) {
        tag.value = tag.value.copy(permissions = tag.value.permissions - permission)
    }

    fun setColor(color: List<Float>) {
        tag.value = tag.value.copy(color = color)
    }

    fun saveTag() = viewModelScope.launch {
        repository.createTag(tag.value)
    }
}