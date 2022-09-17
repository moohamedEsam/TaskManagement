package com.example.taskmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.Dashboard
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.useCases.shared.GetCurrentUserStatusUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getCurrentUserStatusUseCase: GetCurrentUserStatusUseCase
) : ViewModel() {
    private val _isUserAuthorized = MutableStateFlow<Resource<Boolean>>(Resource.Initialized())
    val isUserAuthorized = _isUserAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            _isUserAuthorized.update { getCurrentUserStatusUseCase(Unit) }
        }
    }
}