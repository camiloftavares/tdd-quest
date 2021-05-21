package com.squad.tdd.ui.signin

import androidx.lifecycle.MutableLiveData
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result
import com.squad.tdd.ui.signin.repositories.GoogleRepository
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
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

    lateinit var viewModel: SignInViewModel
    private val googleRepository = mockk<GoogleRepository>()

    private val googleToken = "token"

    @BeforeEach
    fun setUp() {
        viewModel = SignInViewModel(googleRepository)
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

            val verifyGoogle = viewModel.verifyGoogle(googleToken)

            assertThat(verifyGoogle.getOrAwaitValue(), equalTo(result))
        }
    }
}