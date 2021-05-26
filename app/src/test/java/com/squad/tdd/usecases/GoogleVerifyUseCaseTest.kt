package com.squad.tdd.usecases

import androidx.lifecycle.MutableLiveData
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class GoogleVerifyUseCaseTest {

    private val userPreference = mockk<UserPreference>(relaxed = true)
    private val logger = mockk<AppLogger>(relaxed = true)
    private val googleRepository = mockk<GoogleRepository>()

    lateinit var useCase: GoogleVerifyUseCase

    private val userInfo = UserInfo("idToken")

    @BeforeEach
    fun setUp() {
        useCase = GoogleVerifyUseCase(googleRepository, userPreference, logger)
    }

    @Nested
    @DisplayName("Google verify")
    inner class GoogleVerifyFlow {

        @Test
        fun `should return success`() {
            val googleVerify = Result.Success(GoogleVerify("200"))
            verifyResultGivenResponse(
                result = googleVerify,
                response =  MutableLiveData(googleVerify)
            )
            verify { googleRepository.verifyGoogleAccount(any()) }
        }

        @Test
        fun `should return error`() {
            val exception = Exception()
            verifyResultGivenResponse(
                result = Result.ApiError(exception),
                response =  MutableLiveData(Result.ApiError(exception))
            )
        }

        @Test
        fun `should return loading`() {
            verifyResultGivenResponse(
                result = Result.Loading,
                response = MutableLiveData(Result.Loading)
            )
        }

        private fun verifyResultGivenResponse(result: Result<Any>, response: MutableLiveData<Result<GoogleVerify>>) {
            every { googleRepository.verifyGoogleAccount(any()) } returns response

            val verifyGoogle = useCase.verifyGoogle(userInfo)

            verifyGoogle.getOrAwaitValue() shouldBeEqualTo result
        }
    }

    @Nested
    @DisplayName("Save User info")
    inner class SaveUserInfo {

        @Test
        fun `should save when return success`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.Success(GoogleVerify("200"))
            )

            verify { userPreference.saveUserInfo(userInfo) }
        }

        @Test
        fun `should not save when return error`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.ApiError(Exception())
            )

            verify { userPreference wasNot Called }
        }

        @Test
        fun `should not save when return loading`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.Loading
            )

            verify { userPreference wasNot Called }
        }
    }

    @Nested
    @DisplayName("Log User info")
    inner class LogUserInfo {

        @Test
        fun `should log when return success`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.Success(GoogleVerify("200"))
            )

            verify { logger.userSignedIn() }
        }

        @Test
        fun `should not log when return error`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.ApiError(Exception())
            )

            verify { logger wasNot Called }
        }

        @Test
        fun `should not log when return loading`() {
            verifyGoogleWithResultStatus(
                resultStatus = Result.Loading
            )

            verify { logger wasNot Called }
        }

    }

    private fun verifyGoogleWithResultStatus(resultStatus: Result<GoogleVerify>) {
        every { googleRepository.verifyGoogleAccount(any()) } returns
                MutableLiveData(resultStatus)

        useCase.verifyGoogle(userInfo).observeForever { }
    }

    @Nested
    inner class VerifyGoogleUsingFlow {
        private val channel = Channel<Result<GoogleVerify>>()
        private val flow = channel.consumeAsFlow()


        @Test
        fun `should return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))
            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flow
            launch {
                channel.send(successResult)
                channel.close()
            }

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo successResult
            }
        }

        @Test
        fun `should return error`() = runBlockingTest {
            val errorResult = Result.ApiError(Exception())

            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flow
            launch {
                channel.send(errorResult)
                channel.close()
            }

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo errorResult
            }

        }

        @Test
        fun `should return loading`() = runBlockingTest {
            val loadingResult = Result.Loading

            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flow
            launch {
                channel.send(loadingResult)
                channel.close()
            }

            useCase.verifyGoogleCoroutine(userInfo).collect {
                it shouldBeEqualTo loadingResult
            }

        }

        @Test
        fun `should save when return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))

            callVerifyGoogleWithResultStatus(successResult)

            verify { userPreference.saveUserInfo(userInfo) }
        }

        @Test
        fun `should not save when return error`() {
            val errorResult = Result.ApiError(Exception())

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
            every { googleRepository.verifyGoogleAccountFlow(userInfo.idToken) } returns flow
            launch {
                channel.send(resultStatus)
                channel.close()
            }

            useCase.verifyGoogleCoroutine(userInfo).collect()
        }
    }
}