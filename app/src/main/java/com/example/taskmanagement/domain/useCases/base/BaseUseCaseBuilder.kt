package com.example.taskmanagement.domain.useCases.base

import com.example.taskmanagement.domain.dataModels.utils.Resource

abstract class BaseUseCaseBuilder<ParamType, ReturnType> {
    private var isLoading: Boolean = false
    abstract suspend fun build(params: ParamType): Resource<ReturnType>

    suspend operator fun invoke(params: ParamType): Resource<ReturnType> {
        if (isLoading) return Resource.Error("operation already started")
        isLoading = true
        val result = build(params)
        isLoading = false
        return result
    }

}