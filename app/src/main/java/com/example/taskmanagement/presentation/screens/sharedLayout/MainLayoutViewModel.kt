package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.Dashboard
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.useCases.shared.ObserveUserStatusUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserDashboardUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainLayoutViewModel(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val getCurrentUserDashboardUseCase: GetCurrentUserDashboardUseCase,
    private val observeUserStatusUseCase: ObserveUserStatusUseCase
) : ViewModel() {
    private val _userDashboard = MutableStateFlow<Resource<Dashboard>>(Resource.Initialized())
    val userDashboard = _userDashboard.asStateFlow()

    private val _userProfile = MutableStateFlow<Resource<User>>(Resource.Initialized())
    val userProfile = _userProfile.asStateFlow()

    init {
        viewModelScope.launch {
            observeUserStatusUseCase(Unit).collectLatest {
                if (it !is UserStatus.Authorized) return@collectLatest
                _userDashboard.update { getCurrentUserDashboardUseCase(Unit) }
                _userProfile.update { getCurrentUserProfileUseCase(Unit) }
            }
        }
    }
}