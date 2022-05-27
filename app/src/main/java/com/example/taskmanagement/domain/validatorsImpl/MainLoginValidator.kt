package com.example.taskmanagement.domain.validatorsImpl

import android.util.Patterns
import com.example.taskmanagement.domain.data_models.utils.ValidationResult
import com.example.taskmanagement.domain.vallidators.LoginValidator

class MainLoginValidator : LoginValidator {
    override fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() || email.isEmpty() -> ValidationResult(false, "email empty or blank")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(
                false,
                "not valid email pattern"
            )
            else -> ValidationResult(true)
        }
    }

    override fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() || password.isBlank() -> ValidationResult(
                false,
                "password is empty or blank"
            )
            !password.any { it.isLetter() } -> ValidationResult(
                false,
                "password must contains at least one letter"
            )
            password.length < 6 -> ValidationResult(false, "password is too week min length is 6")
            else -> ValidationResult(true)
        }
    }
}