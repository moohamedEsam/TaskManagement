package com.example.taskmanagement.presentation.screens.signUp

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.data_models.utils.RegisterUser
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.domain.data_models.utils.ValidationResult
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.vallidators.LoginValidator
import io.ktor.http.*
import kotlinx.coroutines.launch
import java.io.File

class SignUpViewModel(
    private val repository: MainRepository,
    private val validator: LoginValidator
) : ViewModel() {
    val user = mutableStateOf(RegisterUser("", "", "", null, ""))
    val userStatus = mutableStateOf<UserStatus>(UserStatus.LoggedOut)
    val confirmPassword = mutableStateOf("")
    val usernameValidationResult = mutableStateOf(ValidationResult(true))
    val emailValidationResult = mutableStateOf(ValidationResult(true))
    val passwordValidationResult = mutableStateOf(ValidationResult(true))
    val confirmPasswordValidationResult = mutableStateOf(ValidationResult(true))
    val phoneValidationResult = mutableStateOf(ValidationResult(true))

    fun setEmail(value: String) {
        user.value = user.value.copy(email = value)
    }

    fun setUsername(value: String) {
        user.value = user.value.copy(username = value)
    }

    fun setPassword(value: String) {
        user.value = user.value.copy(password = value)
    }

    fun setPhotoPath(value: String?) {
        user.value = user.value.copy(photoPath = value)
    }

    fun setConfirmPassword(value: String) {
        confirmPassword.value = value
    }

    fun setPhone(value: String) {
        user.value = user.value.copy(phone = value)
    }


    fun submit() = viewModelScope.launch {
        usernameValidationResult.value = validator.validateUsername(user.value.username)
        emailValidationResult.value = validator.validateEmail(user.value.email)
        passwordValidationResult.value = validator.validatePassword(user.value.password)
        confirmPasswordValidationResult.value =
            validator.validateConfirmPassword(user.value.password, confirmPassword.value)
        phoneValidationResult.value = validator.validatePhone(user.value.phone)
        if (usernameValidationResult.value.success &&
            emailValidationResult.value.success &&
            passwordValidationResult.value.success &&
            confirmPasswordValidationResult.value.success &&
            phoneValidationResult.value.success
        ) {
            userStatus.value = UserStatus.Loading
            userStatus.value = repository.registerUser(user.value)

        }

    }

}