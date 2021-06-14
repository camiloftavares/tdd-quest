package com.squad.tdd.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.preferences.UserPreference
import io.mockk.every

open class BaseTestRobot {
    private fun widget(resId: Int) : ViewInteraction = onView(withId(resId))

    private fun textView(resId: Int) : ViewInteraction = widget(resId)

    private fun matchText(viewInteraction: ViewInteraction, text: String) : ViewInteraction = viewInteraction
        .check(matches(withText(text)))

    fun matchText(resId: Int, text: String) :ViewInteraction =
        matchText(textView(resId), text)

    private fun imageView(resId: Int): ViewInteraction = widget(resId)

    fun matchDrawable(resId: Int, drawableId: Int) : ViewInteraction =
        imageView(resId).check(matches(withDrawable(drawableId)))

    fun matchVisible(resId: Int): ViewInteraction = widget(resId).check(matches(isDisplayed()))

    fun launchFragment(fragment: Fragment, navHostController: NavController) {
        launchFragmentInContainer {
            fragment.also { fragment ->
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