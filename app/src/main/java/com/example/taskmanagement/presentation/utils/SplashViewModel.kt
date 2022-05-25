package com.example.taskManagementWithMongoDB.presentation.utils

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class SplashViewModel(private val authRepository: MainRepository) : ViewModel() {
    val loggedIn = mutableStateOf<Boolean?>(null)

    fun userLoggedIn() = viewModelScope.launch {
    }
}