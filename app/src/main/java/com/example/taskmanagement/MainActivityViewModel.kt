package com.example.taskmanagement

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository

class MainActivityViewModel(private val repository: MainRepository): ViewModel() {
    val userStatus = mutableStateOf<UserStatus>(UserStatus.Loading)
    fun isUserStillLoggedIn(context: Context) = repository.isUserStillLoggedIn(context)
}