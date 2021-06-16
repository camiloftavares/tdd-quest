package com.squad.tdd.ui.main

import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.utils.FakeUserPreference
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private val signInHelper = mockk<SignInHelper>(relaxed = true)
    private val navHostController = mockk<NavController>(relaxed = true)
    private val userPreference = FakeUserPreference()

    @Before
    fun setUp() {
        ServiceLocator.userPreference = userPreference
        ServiceLocator.signInHelper = signInHelper
    }

    @Test
    fun shouldGoToSignInFragmentIfUserIsNotLogged() {
        mainScreen {
            userIsNotLogged(signInHelper, userPreference)
            launchMainScreen(navHostController)
            shouldNavigateToSign(navHostController)
        }
    }

    @Test
    fun shouldKeepOnMainFragmentIfUserIsLogged() {
        mainScreen {
            userIsLogged(signInHelper, userPreference)
            launchMainScreen(navHostController)
            shouldNotNavigateToSign(navHostController)
        }
    }

    @Test
    fun shouldShowUserInfoFromViewModelWhenIsLogged() {
        mainScreen {
            userIsLogged(signInHelper, userPreference)
            launchMainScreen(navHostController)
            matchAvatar(R.drawable.ic_avatar)
            matchUserInfo(expectedUserInfo)
        }
    }
}