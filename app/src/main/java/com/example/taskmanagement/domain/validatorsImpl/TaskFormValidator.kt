package com.example.taskmanagement.domain.validatorsImpl

import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.vallidators.Validator
import java.util.*

class TaskFormValidator : BaseValidator() {
    val estimatedTimeValidator = Validator<String> {
        if (it.toIntOrNull() == null)
            ValidationResult(false, "value should be number")
        else if (it.toInt() < 0)
            ValidationResult(false, "time can't be negative")
        else
            ValidationResult(true)
    }

    val dateValidator = Validator<Date>{
        if (Date().after(it))
            ValidationResult(false, "date can't be in past")
        else
            ValidationResult(true)
    }
}