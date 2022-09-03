package com.example.taskmanagement.domain.validatorsImpl

import android.util.Patterns
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.vallidators.Validator

class ProfileValidator {
    val emailValidator = Validator<String> { email ->
        when {
            email.isBlank() || email.isEmpty() -> ValidationResult(false, "email empty or blank")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(
                false,
                "not valid email pattern"
            )
            else -> ValidationResult(true)
        }
    }
    val usernameValidator = Validator<String> { username ->
        when {
            username.isBlank() || username.isEmpty() -> ValidationResult(
                false,
                "username empty or blank"
            )
            username.length < 3 -> ValidationResult(
                false,
                "username length must be more than 3"
            )
            else -> ValidationResult(true)
        }
    }

    val passwordConfirmValidator = Validator<Pair<String, String>> { (password, confirm) ->
        when {
            password.isBlank() || password.isEmpty() -> ValidationResult(
                false,
                "password empty or blank"
            )
            confirm.isBlank() || confirm.isEmpty() -> ValidationResult(
                false,
                "confirm password empty or blank"
            )
            password != confirm -> ValidationResult(
                false,
                "password and confirm password must be equal"
            )
            else -> ValidationResult(true)
        }
    }

    val phoneValidator = Validator<String> { phone ->
        when {
            phone.isBlank() -> ValidationResult(false, "phone can't be blank")
            phone.length < 10 -> ValidationResult(false, "phone length must be at least 10")
            !phone.all { it.isDigit() || it == '+' } -> ValidationResult(
                false,
                "phone number can only contain digits and +"
            )
            else -> ValidationResult(true)
        }
    }

    val passwordValidator = Validator<String> {password->
        when {
            password.isEmpty() || password.isBlank() -> ValidationResult(
                false,
                "password is empty or blank"
            )
            !password.any { it.isLetter() } -> ValidationResult(
                false,
                "password must contains at least one letter"
            )
            password.length < 6 -> ValidationResult(
                false,
                "password is too week min length is 6"
            )
            else -> ValidationResult(true)
        }
    }

}