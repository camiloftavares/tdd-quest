package com.squad.tdd.ui.signin

import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.usecases.GoogleVerifyUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInFragmentTest {

    private val navHostController = mockk<NavController>(relaxed = true)
    private val permissionManager = FakePermissionManager()
    private val signInHelper = mockk<SignInHelper>(relaxed = true)
    private val verifyGoogleUseCase = mockk<GoogleVerifyUseCase>(relaxed = true)

    @Before
    fun setUp() {
        ServiceLocator.permissionManager = permissionManager
        ServiceLocator.signInHelper = signInHelper
        ServiceLocator.googleVerifyUseCase = verifyGoogleUseCase
    }

    @Test
    fun shouldKeepOnSignScreenWhenUserDenyPermissions() {
        permissionManager.isPermissionGranted = false

        signInScreen {
            launchSignInScreen(navHostController)
            signInClick()
            shouldNotNavigateToMainScreen(navHostController)
        }
    }

    @Test
    fun shouldShowSignInIntentWhenPermissionIsGranted() {
        permissionManager.isPermissionGranted = true

        signInScreen {
            launchSignInScreen(navHostController)
            signInClick()
            shouldShowSignInIntent(signInHelper)
        }
    }

    @Test
    fun shouldNavigateToMainScreenWhenUserPerformSignIn() {
        val userInfo = UserInfo("")
        every { signInHelper.signInResult() } returns flowOf(userInfo)
        every { verifyGoogleUseCase.verifyGoogleCoroutine(userInfo) } returns flowOf(Result.Success(GoogleVerify()))

        signInScreen {
            launchSignInScreen(navHostController)
            shouldNavigateToMainScreen(navHostController)
        }
    }

    @Test
    fun shouldNotNavigateToMainScreenWhenSignInFail() {
        val userInfo = UserInfo("")
        every { signInHelper.signInResult() } returns flowOf(userInfo)
        every { verifyGoogleUseCase.verifyGoogleCoroutine(userInfo) } returns flowOf(Result.ApiError("400"))

        signInScreen {
            launchSignInScreen(navHostController)
            shouldNotNavigateToMainScreen(navHostController)
        }
    }
}