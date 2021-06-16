package com.squad.tdd.ui.main

import androidx.navigation.NavController
import com.squad.tdd.R
import com.squad.tdd.data.UserInfo
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.utils.BaseTestRobot
import com.squad.tdd.utils.FakeUserPreference
import io.mockk.every
import io.mockk.verify
import java.lang.Exception

class MainScreenRobot: BaseTestRobot() {

    val expectedUserInfo = UserInfo("idToken", "Caadsamilo", "camilo@gmail.com")

    fun userIsLogged(signInHelper: SignInHelper, userPreference: FakeUserPreference) {
        every { signInHelper.userIsLogged() } returns true
        userPreference.isUserLogged = true
        userPreference.userInfo = expectedUserInfo
    }

    fun userIsNotLogged(signInHelper: SignInHelper, userPreference: FakeUserPreference) {
        every { signInHelper.userIsLogged() } returns false
        userPreference.isUserLogged = false
    }

    fun launchMainScreen(navHostController: NavController) {
        launchFragment(MainFragment(), navHostController)
    }

    fun matchAvatar(drawableId: Int) {
        matchDrawable(R.id.user_avatar, drawableId)
        matchVisible(R.id.user_avatar)
    }

    fun matchUserInfo(userInfo: UserInfo) {
        matchText(R.id.user_name, userInfo.name)
        matchText(R.id.user_email, userInfo.email)
    }

    fun shouldNavigateToSign(navHostController: NavController) {
        verify(exactly = 1) {
            navHostController.navigate(MainFragmentDirections.actionRequireSignin())
        }
    }

    fun shouldNotNavigateToSign(navHostController: NavController) {
        verify(exactly = 0) {
            navHostController.navigate(MainFragmentDirections.actionRequireSignin())
        }
    }
}

fun mainScreen(func: MainScreenRobot.() -> Unit) = MainScreenRobot().apply { func() }