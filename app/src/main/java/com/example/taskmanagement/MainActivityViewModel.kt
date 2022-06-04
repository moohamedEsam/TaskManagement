package com.example.taskmanagement

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MainRepository) : ViewModel() {
    val userStatus = mutableStateOf<UserStatus>(UserStatus.Loading)
    fun isUserStillLoggedIn(context: Context) = viewModelScope.launch {
        userStatus.value =
            if (repository.isUserStillLoggedIn(context)) UserStatus.Authorized else UserStatus.LoggedOut
    }
}