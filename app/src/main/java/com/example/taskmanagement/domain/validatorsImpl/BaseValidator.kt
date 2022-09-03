package com.example.taskmanagement.domain.validatorsImpl

import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.vallidators.Validator

open class BaseValidator {
    val nameValidator = Validator<String> { input ->
        when {
            input.isBlank() -> ValidationResult(false, "name can't be blank")
            input.length < 3 -> ValidationResult(false, "name length is too small")
            else -> ValidationResult(true)
        }
    }

    fun isFormValid(validationResults: List<ValidationResult>) =
        validationResults.all { it.isValid }
}