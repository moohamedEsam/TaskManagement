package com.example.taskmanagement.domain.utils.extensionFunctions

import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.vallidators.Validator

fun User.validate(validator: Validator) = validator.validateUser(this)