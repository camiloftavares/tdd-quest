package com.squad.tdd

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.main_activity.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val navHostController = TestNavHostController(getApplicationContext())

    @Before
    fun setUp() {
        launchActivity<MainActivity>().onActivity {
            navHostController.setGraph(R.navigation.main_nav)
            Navigation.setViewNavController(it.main_nav, navHostController)
        }
    }

    @Test
    fun shouldShowMainFragmentAsDefault() {
        assertThat(navHostController.currentDestination?.id, equalTo(R.id.mainFragment))
    }
}