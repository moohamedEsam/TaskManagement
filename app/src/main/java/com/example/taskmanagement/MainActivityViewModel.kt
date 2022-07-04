package com.example.taskmanagement

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.views.UserView
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: IMainRepository) : ViewModel() {
    val user = mutableStateOf<Resource<User>>(Resource.Initialized())
    fun getCurrentUserProfile() = viewModelScope.launch {
        user.value = repository.getUserProfile()
        Log.i("MainActivityViewModel", "getCurrentUserProfile: ${user.value.data?.username}")
    }
}