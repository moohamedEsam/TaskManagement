package com.example.taskmanagement.domain.utils.extensionFunctions

import com.example.taskmanagement.domain.data_models.User
import com.example.taskmanagement.domain.vallidators.Validator

fun User.validate(validator: Validator) = validator.validateUser(this)