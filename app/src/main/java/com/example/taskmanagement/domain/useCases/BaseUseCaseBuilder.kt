package com.example.taskmanagement.domain.useCases

interface BaseUseCaseBuilder<ParamType, ReturnType> {
    suspend fun build(params: ParamType): ReturnType

    suspend operator fun invoke(params: ParamType) = build(params)

}