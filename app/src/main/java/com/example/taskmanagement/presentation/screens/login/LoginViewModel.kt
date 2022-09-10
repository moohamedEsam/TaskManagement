package com.example.taskmanagement.presentation.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.user.LoginUserUseCase
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val validator: ProfileValidator
) : ViewModel() {
    private val _userStatus: MutableStateFlow<UserStatus> = MutableStateFlow(UserStatus.LoggedOut)
    val userStatus = _userStatus.asStateFlow()
    private val _userCredentials = MutableStateFlow(Credentials("", ""))
    val userCredentials = _userCredentials.asStateFlow()
    private val _usernameValidation = MutableStateFlow(ValidationResult(true))
    val usernameValidation = _usernameValidation.asStateFlow()
    private val _passwordValidation = MutableStateFlow(ValidationResult(true))
    val passwordValidation = _passwordValidation.asStateFlow()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    fun setEmail(value: String) {
        _userCredentials.update { it.copy(email = value) }
        _usernameValidation.update { validator.nameValidator.validate(value) }
    }

    fun setPassword(value: String) {
        _userCredentials.update { it.copy(password = value) }
        _passwordValidation.update { validator.passwordValidator.validate(value) }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _userStatus.update {
                loginUserUseCase(LoginUserUseCase.Params(userCredentials.value)).data
                    ?: return@launch
            }
            if (userStatus.value !is UserStatus.Forbidden) return@launch
            val event = SnackBarEvent(userStatus.value.message ?: "", "Retry") { loginUser() }
            snackBarChannel.send(event)
        }
    }

    fun submit() = viewModelScope.launch {
        if (validator.isFormValid(_usernameValidation.value, _passwordValidation.value))
            loginUser()
    }

}