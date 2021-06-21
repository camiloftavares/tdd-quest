package com.squad.tdd.ui.signin

import androidx.navigation.NavController
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.utils.BaseTestRobot
import io.mockk.verify

class SignInScreenRobot: BaseTestRobot() {

    fun launchSignInScreen(navHostController: NavController) {
        launchFragment(SignInFragment(), navHostController)
    }

    fun signInClick() {
        buttonClick(R.id.sign_in_btn)
    }

    fun shouldNotNavigateToMainScreen(navHostController: NavController) {
        verify(exactly = 0) {
            navHostController.navigate(SignInFragmentDirections.actionMainScreen())
        }
    }

    fun shouldShowSignInIntent(signInHelper: SignInHelper) {
        verify { signInHelper.showSignInIntent() }
    }

    fun shouldNavigateToMainScreen(navHostController: NavController) {
        verify(exactly = 1) {
            navHostController.navigate(SignInFragmentDirections.actionMainScreen())
        }
    }
}

fun signInScreen(func: SignInScreenRobot.() -> Unit) = SignInScreenRobot().apply { func() }