package com.example.taskmanagement.presentation.screens.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.user.LoginUserUseCase
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import com.example.taskmanagement.domain.vallidators.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val validator: ProfileValidator
) : ViewModel() {
    val userStatus = mutableStateOf<UserStatus>(UserStatus.LoggedOut)
    val userCredentials = mutableStateOf(Credentials("", ""))
    val usernameValidation = mutableStateOf(ValidationResult(true))
    val passwordValidation = mutableStateOf(ValidationResult(true))
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    fun setEmail(value: String) {
        userCredentials.value = userCredentials.value.copy(email = value)
    }

    fun setPassword(value: String) {
        userCredentials.value = userCredentials.value.copy(password = value)
    }

    private fun loginUser(context: Context) {
        viewModelScope.launch {
            userStatus.value =
                loginUserUseCase(LoginUserUseCase.Params(userCredentials.value, context))
            if (userStatus.value is UserStatus.Forbidden) {
                val event = SnackBarEvent(userStatus.value.message ?: "", "Retry") {
                    loginUser(context)
                }
                snackBarChannel.send(event)
            }
        }
    }

    fun submit(context: Context) = viewModelScope.launch {
        usernameValidation.value = validator.usernameValidator.validate(userCredentials.value.email)
        passwordValidation.value = validator.passwordValidator.validate(userCredentials.value.password)
        if (usernameValidation.value.isValid && passwordValidation.value.isValid)
            loginUser(context)
    }

}