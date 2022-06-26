package com.example.taskmanagement.presentation.screens.signUp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.SignUpUser
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.vallidators.Validator
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: IMainRepository,
    private val validator: Validator
) : ViewModel() {
    val user = mutableStateOf(SignUpUser("", "", "", null, ""))
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


    fun submit(context: Context) = viewModelScope.launch {
        usernameValidationResult.value = validator.validateUsername(user.value.username)
        emailValidationResult.value = validator.validateEmail(user.value.email)
        passwordValidationResult.value = validator.validatePassword(user.value.password)
        confirmPasswordValidationResult.value =
            validator.validateConfirmPassword(user.value.password, confirmPassword.value)
        phoneValidationResult.value = validator.validatePhone(user.value.phone)
        if (usernameValidationResult.value.isValid &&
            emailValidationResult.value.isValid &&
            passwordValidationResult.value.isValid &&
            confirmPasswordValidationResult.value.isValid &&
            phoneValidationResult.value.isValid
        ) {
            userStatus.value = UserStatus.Loading
            userStatus.value = repository.registerUser(user.value, context)

        }

    }

}