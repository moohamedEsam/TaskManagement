package com.example.taskmanagement.presentation.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.domain.data_models.utils.ValidationResult
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.vallidators.LoginValidator
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: MainRepository,
    private val validator: LoginValidator
) : ViewModel() {
    val userStatus = mutableStateOf<UserStatus>(UserStatus.LoggedOut)
    val userCredentials = mutableStateOf(Credentials("", ""))
    val usernameValidation = mutableStateOf(ValidationResult(true))
    val passwordValidation = mutableStateOf(ValidationResult(true))

    fun setEmail(value: String) {
        userCredentials.value = userCredentials.value.copy(email = value)
    }

    fun setPassword(value: String) {
        userCredentials.value = userCredentials.value.copy(password = value)
    }

    private fun loginUser() = viewModelScope.launch {
        userStatus.value = repository.loginUser(userCredentials.value)
    }

    fun submit() = viewModelScope.launch {
        usernameValidation.value = validator.validateEmail(userCredentials.value.email)
        passwordValidation.value = validator.validatePassword(userCredentials.value.password)
        if (usernameValidation.value.success && passwordValidation.value.success)
            loginUser()
    }

}