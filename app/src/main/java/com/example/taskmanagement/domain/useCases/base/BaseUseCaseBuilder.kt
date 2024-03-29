package com.example.taskmanagement.domain.useCases.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BaseUseCaseBuilder<ParamType, ReturnType> {
    suspend fun build(params: ParamType): ReturnType

    suspend operator fun invoke(
        params: ParamType,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) =
        withContext(dispatcher) {
            build(params)
        }
    suspend operator fun invoke(params: ParamType) = build(params)

}
