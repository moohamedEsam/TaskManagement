package com.example.taskmanagement.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import com.example.taskmanagement.domain.useCases.user.GetUserProfileUseCase
import com.example.taskmanagement.domain.useCases.user.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userId: String
) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Initialized())
    val user = _user.asStateFlow()
    val isCurrentUser = userId == "current"
    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        _user.update {
            if (userId == "current") {
                getCurrentUserProfileUseCase(Unit)
            } else {
                getUserProfileUseCase(userId)
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        logoutUseCase(Unit)
    }

}