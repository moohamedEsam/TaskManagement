package com.example.taskmanagement

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.views.UserView
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MainRepository) : ViewModel() {
    val user = mutableStateOf<Resource<UserView>>(Resource.Initialized())
    fun getCurrentUserProfile() = viewModelScope.launch {
        user.value = repository.getUserProfile()
    }
}