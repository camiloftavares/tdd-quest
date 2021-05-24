package com.squad.tdd.ui.signin

import androidx.lifecycle.MutableLiveData
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result
import com.squad.tdd.ui.signin.data.UserInfo
import com.squad.tdd.ui.signin.preferences.UserPreference
import com.squad.tdd.ui.signin.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class SignInViewModelTest {

    private val userPreference = mockk<UserPreference>(relaxed = true)
    private val logger = mockk<AppLogger>(relaxed = true)
    lateinit var viewModel: SignInViewModel
    private val googleRepository = mockk<GoogleRepository>()
    private val userInfo = UserInfo("idToken")

    @BeforeEach
    fun setUp() {
        viewModel = SignInViewModel(googleRepository, userPreference, logger)
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

            val verifyGoogle = viewModel.verifyGoogle(userInfo)

            assertThat(verifyGoogle.getOrAwaitValue(), equalTo(result))
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

        viewModel.verifyGoogle(userInfo).observeForever { }
    }
}