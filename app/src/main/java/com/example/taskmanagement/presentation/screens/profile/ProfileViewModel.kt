package com.example.taskmanagement.presentation.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.vallidators.Validator
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: IMainRepository,
    private val validator: Validator
) : ViewModel() {
    val user = mutableStateOf<Resource<User>>(Resource.Initialized())

    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        user.value = repository.getUserProfile()
    }

    fun setUsername(username: String) {
        user.value = user.value.copy(data = user.value.data?.copy(username = username))
    }

    fun setPhone(phone: String) {
        user.value = user.value.copy(data = user.value.data?.copy(phone = phone))
    }

    fun setPhotoPath(photoPath: String) {
        user.value = user.value.copy(data = user.value.data?.copy(photoPath = photoPath))
    }


}