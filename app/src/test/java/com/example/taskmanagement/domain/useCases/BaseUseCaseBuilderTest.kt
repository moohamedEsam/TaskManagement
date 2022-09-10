package com.example.taskmanagement.domain.useCases

import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BaseUseCaseBuilderTest {
    private val useCase = FakeUseCase()

    @Test
    fun `useCaseInvoke - second invoke returns error - invoke use case again before it ends`(): Unit =
        runBlocking {
            launch {
                val result = useCase(Unit)
                assertThat(result).isInstanceOf(Resource.Success::class.java)
            }
            launch {
                val result = useCase(Unit)
                assertThat(result).isInstanceOf(Resource.Error::class.java)
            }
        }

    @Test
    fun `useCaseInvoke - second invoke returns success - invoke use case again after it ends`(): Unit =
        runBlocking {
            launch {
                val result = useCase(Unit)
                assertThat(result).isInstanceOf(Resource.Success::class.java)
            }.join()
            launch {
                val result = useCase(Unit)
                assertThat(result).isInstanceOf(Resource.Success::class.java)
            }
        }
}

private class FakeUseCase : BaseUseCaseBuilder<Unit, List<Int>>() {
    override suspend fun build(params: Unit): Resource<List<Int>> {
        delay(2000)
        return Resource.Success(listOf(1, 2, 3))
    }
}