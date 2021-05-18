package com.squad.tdd.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import io.mockk.every
import io.mockk.mockk
import kotlinx.android.synthetic.main.main_activity.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private val signInHelper = mockk<SignInHelper>()
    private val navHostController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Ignore
    @Test
    fun shouldGoToSignInFragmentIfUserIsNotLogged() {
        every { signInHelper.userIsLogged() } returns false

        launchFragmentInContainer<MainFragment>().onFragment {
            navHostController.setGraph(R.navigation.main_nav)
            Navigation.setViewNavController(it.requireView(), navHostController)
        }

        assertThat(
            navHostController.currentDestination?.id, equalTo(R.id.signInFragment)
        )
    }

}