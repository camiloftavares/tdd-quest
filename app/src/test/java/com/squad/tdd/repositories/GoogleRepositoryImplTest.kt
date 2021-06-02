package com.squad.tdd.repositories

import com.squad.tdd.api.ApiService
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class GoogleRepositoryImplTest {
    private val service = mockk<ApiService>(relaxed = true)

    private val idToken = "idToken"

    private lateinit var repository: GoogleRepository

    @BeforeEach
    fun setUp() {
        repository = GoogleRepositoryImpl(service)
    }

    @Test
    fun `should return loading as first result status`() = runBlockingTest {
        val successResult = GoogleVerify()
        coEvery { service.verifyGoogle(idToken) } returns flowOf(successResult)

        val verifyGoogle = repository.verifyGoogleAccountFlow(idToken)

        verifyGoogle.first() shouldBeEqualTo Result.Loading
    }

    @Test
    fun `should emit success after loading result status`() = runBlockingTest {
        val successResult = GoogleVerify()
        coEvery { service.verifyGoogle(idToken) } returns flowOf(successResult)

        val resultEmission = repository.verifyGoogleAccountFlow(idToken).drop(1).first()

        assertEquals(resultEmission, Result.Success(successResult))
    }

    @Test
    fun `should emit error result status`() = runBlockingTest {
        val errorResult = GoogleVerify("400")
        coEvery { service.verifyGoogle(idToken) } returns flowOf(errorResult)

        val resultEmission = repository.verifyGoogleAccountFlow(idToken).drop(1).first()

        assertEquals(resultEmission, Result.ApiError(errorResult.code))
    }
}