package com.squad.tdd.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Test
    fun checkLoadFragment() {

        launchFragmentInContainer<MainFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.message_value))
            .check(ViewAssertions.matches(ViewMatchers.withText("MainFragment")))
    }
}