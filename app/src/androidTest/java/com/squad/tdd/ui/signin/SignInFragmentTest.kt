package com.squad.tdd.ui.signin

import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.di.ServiceLocator
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInFragmentTest {

    private val navHostController = mockk<NavController>(relaxed = true)
    private val permissionManager = FakePermissionManager()

    @Before
    fun setUp() {
        ServiceLocator.permissionManager = permissionManager
    }

    @Test
    fun shouldKeepOnSignScreenWhenUserDenyPermissions() {
        permissionManager.isPermissionDenied = true

        signInScreen {
            launchSignInScreen(navHostController)
            signInClick()
            shouldNotNavigateToMainScreen(navHostController)
        }
    }

    @Test
    fun shouldNavigateToMainScreenWhenPermissionIsGranted() {
        permissionManager.isPermissionDenied = false

        signInScreen {
            launchSignInScreen(navHostController)
            signInClick()
            shouldNavigateToMainScreen(navHostController)
        }
    }
}