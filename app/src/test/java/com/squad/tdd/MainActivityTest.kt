package com.squad.tdd

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun `should navigate to main fragment()`() {
        val mainActivityScenario = launchActivity<MainActivity>()
        mainActivityScenario.onActivity {

        }
    }
}