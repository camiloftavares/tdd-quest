package com.squad.tdd.ui.signin

import androidx.lifecycle.MutableLiveData
import com.squad.tdd.MainCoroutineRule
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.usecases.GoogleVerifyUseCase
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class SignInViewModelTest {

    @get:Rule var rule = MainCoroutineRule()

    lateinit var viewModel: SignInViewModel
    private val googleVerifyUseCase = mockk<GoogleVerifyUseCase>()
    private val userInfo = UserInfo("idToken")

    @BeforeEach
    fun setUp() {
        viewModel = SignInViewModel(googleVerifyUseCase)
    }

    @Nested
    @Ignore
    @DisplayName("Google verify")
    inner class GoogleVerifyFlow {

        @Test
        fun `should return success`() {
            val googleVerify = Result.Success(GoogleVerify("200"))
            verifyResultGivenResponse(
                result = googleVerify,
                response =  MutableLiveData(googleVerify)
            )
            verify { googleVerifyUseCase.verifyGoogle(any()) }
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
            every { googleVerifyUseCase.verifyGoogle(any()) } returns response

            val verifyGoogle = viewModel.verifyGoogle(userInfo)

            verifyGoogle.getOrAwaitValue() shouldBeEqualTo result
        }
    }


    @Nested
    inner class VerifyGoogleUsingFlow {
        private val channel = Channel<Result<GoogleVerify>>()
        private val flow = channel.consumeAsFlow()


        @Test
        fun `should return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))

            every { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(successResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()

            verifyGoogleCoroutine shouldBeEqualTo successResult
        }

        @Test
        fun `should return error`() = runBlockingTest {
            val errorResult = Result.ApiError(Exception())

            every { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(errorResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()

            verifyGoogleCoroutine shouldBeEqualTo errorResult
        }

        @Test
        fun `should return loading`() = runBlockingTest {
            val loadingResult = Result.Loading

            every { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(loadingResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()

            verifyGoogleCoroutine shouldBeEqualTo loadingResult
        }
    }

}