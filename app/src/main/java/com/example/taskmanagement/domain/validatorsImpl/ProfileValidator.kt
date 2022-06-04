package com.example.taskmanagement.domain.validatorsImpl

import android.util.Patterns
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.vallidators.Validator

class ProfileValidator : Validator {
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

    override fun validateUsername(username: String): ValidationResult {
        return when{
            username.isBlank() || username.isEmpty() -> ValidationResult(false, "username empty or blank")
            username.length < 3 -> ValidationResult(false, "username length must be more than 3")
            else -> ValidationResult(true)
        }
    }

    override fun validateConfirmPassword(password: String, confirm: String): ValidationResult {
        return when{
            password.isBlank() || password.isEmpty() -> ValidationResult(false, "password empty or blank")
            confirm.isBlank() || confirm.isEmpty() -> ValidationResult(false, "confirm password empty or blank")
            password != confirm -> ValidationResult(false, "password and confirm password must be equal")
            else -> ValidationResult(true)
        }
    }

    override fun validatePhone(phone: String?): ValidationResult {
        return when{
            phone.isNullOrBlank() || phone.isNullOrEmpty() -> ValidationResult(false, "phone empty or blank")
            phone.length < 10 -> ValidationResult(false, "phone length must be at least 10")
            else -> ValidationResult(true)
        }
    }

    override fun validateUser(user: User): ValidationResult {
        return ValidationResult(true)
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