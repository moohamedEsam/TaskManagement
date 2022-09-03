package com.example.taskmanagement.presentation.screens.signUp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.SignUpUserBody
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.user.SignUpUseCase
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import com.example.taskmanagement.domain.vallidators.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val validator: ProfileValidator
) : ViewModel() {
    val user = mutableStateOf(SignUpUserBody("", "", "", null, ""))
    val userStatus = mutableStateOf<UserStatus>(UserStatus.LoggedOut)
    val confirmPassword = MutableStateFlow("")
    val usernameValidationResult = MutableStateFlow(ValidationResult(true))
    val emailValidationResult = MutableStateFlow(ValidationResult(true))
    val passwordValidationResult = MutableStateFlow(ValidationResult(true))
    val confirmPasswordValidationResult = MutableStateFlow(ValidationResult(true))
    val phoneValidationResult = MutableStateFlow(ValidationResult(true))
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
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


    fun submit(context: Context) {
        viewModelScope.launch {
            usernameValidationResult.value =
                validator.usernameValidator.validate(user.value.username)
            emailValidationResult.value = validator.emailValidator.validate(user.value.email)
            passwordValidationResult.value =
                validator.passwordValidator.validate(user.value.password)
            confirmPasswordValidationResult.value =
                validator.passwordConfirmValidator.validate(user.value.password to confirmPassword.value)
            phoneValidationResult.value = validator.phoneValidator.validate(user.value.phone?:"")
            if (usernameValidationResult.value.isValid &&
                emailValidationResult.value.isValid &&
                passwordValidationResult.value.isValid &&
                confirmPasswordValidationResult.value.isValid &&
                phoneValidationResult.value.isValid
            ) {
                userStatus.value = UserStatus.Loading
                userStatus.value = signUpUseCase(SignUpUseCase.Params(user.value, context))
                if (userStatus.value is UserStatus.Forbidden) {
                    val event = SnackBarEvent(userStatus.value.message ?: "", "Retry") {
                        submit(context)
                    }
                    snackBarChannel.send(event)
                }
            }

        }
    }

}