package com.squad.tdd.ui.main

import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import com.squad.tdd.data.UserInfo
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.ui.main.MainFragmentDirections.Companion.actionRequireSignin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private val signInHelper = mockk<SignInHelper>(relaxed = true)
    private val navHostController = mockk<NavController>(relaxed = true)
    private val userPreference =  mockk<UserPreference>(relaxed = true)
    private val expectedUserInfo = UserInfo("idToken", "Camilo", "camilo@gmail.com")

    @Before
    fun setUp() {
        ServiceLocator.userPreference = userPreference
        ServiceLocator.signInHelper = signInHelper
        every { signInHelper.userIsLogged() } returns true
        every { userPreference.getUserInfo() } returns flowOf(expectedUserInfo)
    }

    @Test
    fun shouldGoToSignInFragmentIfUserIsNotLogged() {
        every { signInHelper.userIsLogged() } returns false

        mainScreen {
            launchMainScreen(navHostController)
        }

        verify(exactly = 1) {
            navHostController.navigate(actionRequireSignin())
        }
    }

    @Test
    fun shouldKeepOnMainFragmentIfUserIsLogged() {
        every { signInHelper.userIsLogged() } returns true

        mainScreen {
            launchMainScreen(navHostController)
        }

        verify(exactly = 0) {
            navHostController.navigate(actionRequireSignin())
        }
    }

    @Test
    fun shouldShowUserInfoFromViewModelWhenIsLogged() = runBlockingTest {
        mainScreen {
            launchMainScreen(navHostController)
            matchAvatar(R.drawable.ic_avatar)
            matchName(expectedUserInfo.name)
            matchEmail(expectedUserInfo.email)
        }
    }
}