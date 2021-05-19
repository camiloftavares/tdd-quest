package com.squad.tdd.ui.signin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.squad.tdd.R
import com.squad.tdd.helpers.PermissionHelper
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInFragmentTest {

    private val navHostController = mockk<NavController>(relaxed = true)
    private val permissionHelper = mockk<PermissionHelper>(relaxed = true)
    private lateinit var uiDevice: UiDevice

    @Before
    fun setUp() {

        //InstrumentationRegistry.getInstrumentation().uiAutomation.
        //executeShellCommand("pm revoke ${getTargetContext().packageName} android.permission.ACCESS_FINE_LOCATION")

        launchFragment()
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        every {
            permissionHelper.isLocationPermissionGranted()
        } returns false
    }

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.
        executeShellCommand("pm revoke ${getTargetContext().packageName} android.permission.ACCESS_FINE_LOCATION")

    }

    @Test
    fun shouldRequestPermissionWhenUserClickSignInWithoutPermissionGranted() {
        onView(withId(R.id.sign_in_btn)).perform(click())
        assertViewWithTextIsVisible(uiDevice, "ALLOW")
    }

    private fun assertViewWithTextIsVisible(uiDevice: UiDevice, text: String) {
        val allowButton = uiDevice.findObject(UiSelector().text(text))

        Assert.assertEquals(allowButton.exists(), true)
    }

    private fun launchFragment() {
        launchFragmentInContainer {
            SignInFragment().also { fragment ->
                //fragment.permissionHelper = permissionHelper
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