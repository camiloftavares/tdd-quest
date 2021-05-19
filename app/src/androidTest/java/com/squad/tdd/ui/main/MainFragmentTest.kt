package com.squad.tdd.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.ui.main.MainFragmentDirections.Companion.actionRequireSignin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private val signInHelper = mockk<SignInHelper>(relaxed = true)
    private val navHostController = mockk<NavController>(relaxed = true)

    @Test
    fun shouldGoToSignInFragmentIfUserIsNotLogged() {
        every { signInHelper.userIsLogged() } returns false

        launchFragment()

        verify(exactly = 1) {
            navHostController.navigate(actionRequireSignin())
        }
    }

    @Test
    fun shouldKeepOnMainFragmentIfUserIsLogged() {
        every { signInHelper.userIsLogged() } returns true

        launchFragment()

        verify(exactly = 0) {
            navHostController.navigate(actionRequireSignin())
        }
    }

    private fun launchFragment() {
        launchFragmentInContainer {
            MainFragment().also { fragment ->
                fragment.signInHelper = signInHelper
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        navHostController.setGraph(R.navigation.main_nav)
                        Navigation.setViewNavController(fragment.requireView(), navHostController)
                    }
                }
            }
        }
    }

}