package com.example.taskmanagement.presentation.screens.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.SignUpUserBody
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.user.SignUpUseCase
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val validator: ProfileValidator
) : ViewModel() {
    private val _user = MutableStateFlow(SignUpUserBody("", "", null, null, ""))
    val user = _user.asStateFlow()
    private val _userStatus = MutableStateFlow<UserStatus>(UserStatus.LoggedOut)
    val userStatus = _userStatus.asStateFlow()
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()
    private val _usernameValidationResult = MutableStateFlow(ValidationResult(true))
    val usernameValidationResult = _usernameValidationResult.asStateFlow()
    private val _emailValidationResult = MutableStateFlow(ValidationResult(true))
    val emailValidationResult = _emailValidationResult.asStateFlow()
    private val _passwordValidationResult = MutableStateFlow(ValidationResult(true))
    val passwordValidationResult = _passwordValidationResult.asStateFlow()
    private val _confirmPasswordValidationResult = MutableStateFlow(ValidationResult(true))
    val confirmPasswordValidationResult = _confirmPasswordValidationResult.asStateFlow()
    private val _phoneValidationResult = MutableStateFlow(ValidationResult(true))
    val phoneValidationResult = _phoneValidationResult.asStateFlow()

    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    fun setEmail(value: String) {
        _user.update { it.copy(email = value) }
        _emailValidationResult.update { validator.emailValidator.validate(value) }
    }

    fun setUsername(value: String) {
        _user.update { it.copy(username = value) }
        _usernameValidationResult.update { validator.nameValidator.validate(value) }
    }

    fun setPassword(value: String) {
        _user.update { it.copy(password = value) }
        _passwordValidationResult.update { validator.passwordValidator.validate(value) }
    }

    fun setPhotoPath(value: String?) {
        _user.update { it.copy(photoPath = value) }
    }

    fun setConfirmPassword(value: String) {
        _confirmPassword.update { value }
        _confirmPasswordValidationResult.update { validator.passwordConfirmValidator.validate(_user.value.password to _confirmPassword.value) }
    }

    fun setPhone(value: String) {
        _user.update { it.copy(phone = value) }
        _phoneValidationResult.update { validator.phoneValidator.validate(value) }
    }


    fun submit() {
        viewModelScope.launch {
            val formValid = validator.isFormValid(
                _usernameValidationResult.value,
                _passwordValidationResult.value,
                _confirmPasswordValidationResult.value,
                _emailValidationResult.value,
                _phoneValidationResult.value
            )
            if (!formValid)
                return@launch
            _userStatus.update { signUpUseCase(SignUpUseCase.Params(_user.value)) }
            if (userStatus.value !is UserStatus.Forbidden) return@launch
            val event = SnackBarEvent(userStatus.value.message ?: "", "Retry") { submit() }
            snackBarChannel.send(event)

        }
    }

}