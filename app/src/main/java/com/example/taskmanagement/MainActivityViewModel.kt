package com.example.taskmanagement

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MainRepository) : ViewModel() {
    val user = mutableStateOf<Resource<User>>(Resource.Initialized())
    fun getCurrentUserProfile() = viewModelScope.launch {
        user.value = repository.getUserProfile()
        Log.i("MainActivityViewModel", "getCurrentUserProfile: ${user.value.data?.username}")
    }
}