package com.squad.tdd.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.squad.tdd.MainCoroutineRule
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.usecases.GoogleVerifyUseCaseImpl
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class SignInViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    lateinit var viewModel: SignInViewModel
    private val googleVerifyUseCase = mockk<GoogleVerifyUseCaseImpl>()
    private val userInfo = UserInfo("idToken", "name", "email", "avatar")

    @BeforeEach
    fun setUp() {
        viewModel = SignInViewModel(googleVerifyUseCase)
    }

    @Nested
    inner class VerifyGoogleUsingFlow {
        private val channel = Channel<Result<GoogleVerify>>()
        private val flow = channel.consumeAsFlow()


        @Test
        fun `should return success`() = runBlockingTest {
            val successResult = Result.Success(GoogleVerify("200"))

            coEvery { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(successResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()

            verifyGoogleCoroutine shouldBeEqualTo successResult
        }

        @Test
        fun `should return error`() = runBlockingTest {
            val errorResult = Result.ApiError("400")

            coEvery { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(errorResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()

            verifyGoogleCoroutine shouldBeEqualTo errorResult
        }

        @Test
        fun `should return loading`() = runBlockingTest {
            val loadingResult = Result.Loading

            coEvery { googleVerifyUseCase.verifyGoogleCoroutine(userInfo) } returns flow
            launch { channel.send(loadingResult) }

            val verifyGoogleCoroutine = viewModel.verifyGoogleCoroutine(userInfo).getOrAwaitValue()
            val returned = viewModel.verifyGoogleCoroutine(userInfo)
            returned.observeForTesting {
                returned.value shouldBeEqualTo  loadingResult
            }

            verifyGoogleCoroutine shouldBeEqualTo loadingResult
        }
    }

}

fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> { Unit }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}