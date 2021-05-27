package com.squad.tdd.ui

import android.Manifest
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.squad.tdd.MainActivity
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
            activity.permissionManager.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }

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
            activity.permissionManager.callPermissionBoundedAction(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                grantedFlag = true
            }, onPermissionDenied = {
                deniedFlag = true
            })
        }

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
            activity.permissionManager.callPermissionBoundedAction(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                grantedFlag = true
            }, onPermissionDenied = {
                deniedFlag = true
            })
        }

        assertDialogAndDenyPermission(uiDevice)

        Assert.assertThat(grantedFlag, equalTo(false))
        Assert.assertThat(deniedFlag, equalTo(true))
    }


    @Test
    fun shouldCallPermissionBoundedActionForChainedPermissionRequest() {

        // Given permission was not already granted for camera and location
        var flag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.callPermissionBoundedAction(Manifest.permission.ACCESS_FINE_LOCATION,
                    onPermissionGranted = {
                activity.permissionManager.callPermissionBoundedAction(Manifest.permission.CAMERA,
                    onPermissionGranted = {
                    flag = true
                })
            })
        }

        assertDialogAndAllowPermission(uiDevice)
        assertDialogAndAllowPermission(uiDevice)

        Assert.assertThat(flag, equalTo(true))
    }

    @Test
    fun shouldCallPermissionBoundedActionWhenPermissionIsAlreadyGranted() {

        // Given permission was already granted
        var flag = false

        launchActivityAndThen { activity ->
            activity.permissionManager.callPermissionBoundedAction(Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                flag = true
            })
        }

        Assert.assertThat(flag, equalTo(true))
    }

    private fun launchActivityAndThen(onActivityLaunched: (activity: MainActivity) -> Unit) {
        val scenario = launchActivity<MainActivity>()

        scenario.onActivity { activity ->
            onActivityLaunched(activity)
        }
    }

    private fun clickButtonWithText(uiDevice: UiDevice, text: String) {
        val allowButton = uiDevice.findObject(UiSelector().text(text))

        if (allowButton.exists()) {
            allowButton.click()
        }
    }

    private fun assertViewWithTextIsVisible(uiDevice: UiDevice, text: String) {
        val allowButton = uiDevice.findObject(UiSelector().text(text))

        Assert.assertThat(allowButton.exists(), equalTo(true))
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