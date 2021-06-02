package com.squad.tdd.ui.main

import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.utils.InstantExecutorExtension
import com.squad.tdd.utils.getOrAwaitValue
import com.squad.tdd.utils.shouldBeEqualTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class MainViewModelTest {

    private val userPreference = mockk<UserPreference>()
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setUp() {
        viewModel = MainViewModel(userPreference)
    }

    @Test
    fun `should return a user info from user preference`() {
        val expectedUserInfo = UserInfo("idToken")
        every { userPreference.getUserInfo() } returns flowOf(expectedUserInfo)

        val userInfo = viewModel.userInfo.getOrAwaitValue()

        verify { userPreference.getUserInfo() }
        userInfo shouldBeEqualTo expectedUserInfo
    }
}