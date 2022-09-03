package com.example.taskmanagement.domain.vallidators

import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult

fun interface Validator<T> {
    fun validate(input: T): ValidationResult
}