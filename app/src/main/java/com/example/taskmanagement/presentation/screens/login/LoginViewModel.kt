package com.example.taskmanagement.presentation.screens.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.LoginUserUseCase
import com.example.taskmanagement.domain.vallidators.Validator
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val validator: Validator
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

    private fun loginUser(context: Context) = viewModelScope.launch {
        userStatus.value = loginUserUseCase(LoginUserUseCase.Params(userCredentials.value, context))
    }

    fun submit(context: Context) = viewModelScope.launch {
        usernameValidation.value = validator.validateUsername(userCredentials.value.email)
        passwordValidation.value = validator.validatePassword(userCredentials.value.password)
        if (usernameValidation.value.isValid && passwordValidation.value.isValid)
            loginUser(context)
    }

}