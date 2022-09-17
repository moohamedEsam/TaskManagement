package com.example.taskmanagement.presentation.screens.profile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.Token
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.projects.GetCurrentUserProjectUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import com.example.taskmanagement.domain.useCases.user.LogoutUseCase
import com.example.taskmanagement.domain.vallidators.Validator
import com.example.taskmanagement.presentation.koin.saveToken
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val user = mutableStateOf<Resource<User>>(Resource.Initialized())

    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        user.value = getCurrentUserProfileUseCase(Unit)
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

    fun signOut() = viewModelScope.launch {
        logoutUseCase(Unit)
    }

}