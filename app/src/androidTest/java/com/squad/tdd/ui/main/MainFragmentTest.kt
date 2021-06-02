package com.squad.tdd.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import com.squad.tdd.data.UserInfo
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.ui.main.MainFragmentDirections.Companion.actionRequireSignin
import com.squad.tdd.utils.ViewModelUtil
import com.squad.tdd.utils.withDrawable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private val signInHelper = mockk<SignInHelper>(relaxed = true)
    private val viewModel = mockk<MainViewModel>()
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

    @Test
    fun shouldShowUserInfoFromViewModelWhenIsLogged() {
        every { signInHelper.userIsLogged() } returns true
        val expectedUserInfo = UserInfo("")
//        every { viewModel.getUserInfoMethod() } returns MutableLiveData(expectedUserInfo)

        launchFragment()

        onView(withId(R.id.user_avatar)).check(matches(isDisplayed()))
        onView(withId(R.id.user_avatar)).check(matches(not(withDrawable(R.drawable.ic_avatar))))
        onView(withId(R.id.user_name)).check(matches(withText("Name")))
        onView(withId(R.id.user_email)).check(matches(withText("email@email.com")))
    }

    private fun launchFragment() {
        launchFragmentInContainer {
            MainFragment().also { fragment ->
                fragment.signInHelper = signInHelper
                fragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
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