package com.squad.tdd.ui

import android.Manifest
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.squad.tdd.MainActivity
import com.squad.tdd.R
import com.squad.tdd.helpers.PermissionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionsManagerTest {

    private lateinit var uiDevice: UiDevice

    @Before
    fun setUp() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun shouldRequestArbitraryPermissionsWhenRequestedPermissionIsNotGrantedYet() {

        // Given permission was not granted
        var permissionManager: PermissionManager? = null
        launchActivityAndThen  { activity ->
            permissionManager = activity.permissionManager
            permissionManager?.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        //clickButtonWithText(uiDevice, "Don't ask again")
        assertDialogAndDenyPermission(uiDevice)

        Assert.assertThat(permissionManager?.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION),
            equalTo(false))
    }

    @Test
    fun shouldCallPermissionGrantedWhenPermissionIsRequestedAndGranted() {

        // Given permission was not already granted
        var grantedFlag = false
        var deniedFlag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                grantedFlag = true
            }, onPermissionDenied = {
                deniedFlag = true
            })
        }

        clickButtonWithText(uiDevice, "OK")

        assertDialogAndAllowPermission(uiDevice)

        Assert.assertThat(grantedFlag, equalTo(true))
        Assert.assertThat(deniedFlag, equalTo(false))
    }

    @Test
    fun shouldCallPermissionDeniedWhenPermissionIsRequestedAndNotGranted() {

        // Given permission was not already granted
        var grantedFlag = false
        var deniedFlag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                grantedFlag = true
            }, onPermissionDenied = {
                deniedFlag = true
            })
        }

        clickButtonWithText(uiDevice, "OK")

        assertDialogAndDenyPermission(uiDevice)

        Assert.assertThat(grantedFlag, equalTo(false))
        Assert.assertThat(deniedFlag, equalTo(true))
    }


    @Test
    fun shouldCallPermissionBoundedActionForChainedPermissionRequest() {

        // Given permission was not already granted for camera and location
        var flag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    onPermissionGranted = {
                activity.permissionManager.handlePermission(Manifest.permission.CAMERA,
                    onPermissionGranted = {
                    flag = true
                })
            })
        }

        clickButtonWithText(uiDevice, "OK")
        assertDialogAndAllowPermission(uiDevice)

        clickButtonWithText(uiDevice, "OK")
        assertDialogAndAllowPermission(uiDevice)

        Assert.assertThat(flag, equalTo(true))
    }

    @Test
    fun shouldCallPermissionBoundedActionWhenPermissionIsAlreadyGranted() {

        // Given permission was already granted
        var flag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                flag = true
            })
        }

        assertPermissionDialogWasNotCreated(uiDevice)
        Assert.assertThat(flag, equalTo(true))
    }

    @Test
    fun shouldShowPermissionRationaleWhenPermissionIsRequestedAfterBeingDenied() {

        // given permission is not granted yet
        var permissionManager: PermissionManager? = null
        launchActivityAndThen { activity ->
            permissionManager = activity.permissionManager
            permissionManager?.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        assertDialogAndDenyPermission(uiDevice)

        permissionManager?.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION)

        runBlocking {
            delay(100)
        }
        assertViewWithTextIsVisible(uiDevice, getTextFromResource(R.string.permission_rationale_default_text))
        clickButtonWithText(uiDevice, "OK")
    }

    @Test
    fun shouldNotShowPermissionDialogWhenDontShowAgainCheckBoxIsClicked() {

        // Given permission is not granted and 'Dont ask again' checkbox was not clicked yet
        var permissionManager: PermissionManager? = null
        launchActivityAndThen { activity ->
            permissionManager = activity.permissionManager
            permissionManager?.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        clickButtonWithText(uiDevice, "OK")

        clickButtonWithText(uiDevice, "Don't ask again")

        assertDialogAndDenyPermission(uiDevice)

        permissionManager?.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION)

        assertViewWithTextIsNotVisible(uiDevice, "ALLOW")
    }

    private fun getTextFromResource(resourceId: Int): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.getString(resourceId)
    }

    private fun launchActivityAndThen(onActivityLaunched: (activity: MainActivity) -> Unit) {
        val scenario = launchActivity<MainActivity>()

        scenario.onActivity { activity ->
            onActivityLaunched(activity)
        }
    }

    private fun clickButtonWithText(uiDevice: UiDevice, text: String) {
        val button = uiDevice.findObject(UiSelector().text(text))

        if (button.exists()) {
            button.click()
        }
    }

    private fun assertViewWithTextIsVisible(uiDevice: UiDevice, text: String) {
        val allowButton = uiDevice.findObject(UiSelector().text(text))

        Assert.assertThat(allowButton.exists(), equalTo(true))
    }

    private fun assertViewWithTextIsNotVisible(uiDevice: UiDevice, text: String) {
        val allowButton = uiDevice.findObject(UiSelector().text(text))

        Assert.assertThat(allowButton.exists(), equalTo(false))
    }

    private fun assertPermissionDialogWasNotCreated(uiDevice: UiDevice) {
        assertViewWithTextIsNotVisible(uiDevice, "ALLOW")
    }

    private fun assertDialogAndAllowPermission(uiDevice: UiDevice) {
        assertViewWithTextIsVisible(uiDevice, "ALLOW")
        assertViewWithTextIsVisible(uiDevice, "DENY")
        clickButtonWithText(uiDevice, "ALLOW")
    }

    private fun assertDialogAndDenyPermission(uiDevice: UiDevice) {
        assertViewWithTextIsVisible(uiDevice, "ALLOW")
        assertViewWithTextIsVisible(uiDevice, "DENY")
        clickButtonWithText(uiDevice, "DENY")
    }

}