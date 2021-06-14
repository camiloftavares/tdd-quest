package com.squad.tdd.usecases

import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class GoogleVerifyUseCaseTest {

    private val userPreference = mockk<UserPreference>(relaxed = true)
    private val logger = mockk<AppLogger>(relaxed = true)
    private val googleRepository = mockk<GoogleRepository>()

    lateinit var useCase: GoogleVerifyUseCaseImpl

    private val userInfo = UserInfo("idToken")
    val errorCode = "400"

    @BeforeEach
    fun setUp() {
        useCase = GoogleVerifyUseCaseImpl(googleRepository, userPreference, logger)
    }

    // TODO: Logger tests using Coroutines
    // TODO: Save User tests using Coroutines

    @Nested
    inner class VerifyGoogleUsingFlow {
        private val channel = Channel<Result<GoogleVerify>>()
        private val flow = channel.consumeAsFlow()


        @Test
        fun `should return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))
            coEvery { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flowOf(successResult)

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo successResult
            }
        }

        @Test
        fun `should return error`() = runBlockingTest {
            val errorResult = Result.ApiError("")

            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flowOf(errorResult)

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo errorResult
            }

        }

        @Test
        fun `should return loading`() = runBlockingTest {
            val loadingResult = Result.Loading

            coEvery { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flowOf(loadingResult)

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo loadingResult
            }

        }

        @Test
        fun `should save when return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))

            callVerifyGoogleWithResultStatus(successResult)

            coVerify { userPreference.saveUserInfo(userInfo) }
        }

        @Test
        fun `should not save when return error`() {
            val errorResult = Result.ApiError("")

            callVerifyGoogleWithResultStatus(errorResult)

            verify { userPreference wasNot Called }
        }

        @Test
        fun `should not save when return loading`() {
            val loadingResult = Result.Loading

            callVerifyGoogleWithResultStatus(loadingResult)

            verify { userPreference wasNot Called }
        }

        private fun callVerifyGoogleWithResultStatus(resultStatus: Result<GoogleVerify>) = runBlockingTest {
            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns
                    flowOf(resultStatus)

                useCase.verifyGoogleCoroutine(userInfo).collect()
            }
    }
}