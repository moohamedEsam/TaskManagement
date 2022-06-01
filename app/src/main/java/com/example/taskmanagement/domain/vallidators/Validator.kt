package com.example.taskmanagement.domain.vallidators

import com.example.taskmanagement.domain.data_models.User
import com.example.taskmanagement.domain.data_models.utils.ValidationResult

interface Validator {
    fun validateEmail(email: String): ValidationResult
    fun validatePassword(password: String): ValidationResult
    fun validateUsername(username: String): ValidationResult
    fun validateConfirmPassword(password: String, confirm: String): ValidationResult
    fun validatePhone(phone: String?): ValidationResult
    fun validateUser(user: User): ValidationResult
}