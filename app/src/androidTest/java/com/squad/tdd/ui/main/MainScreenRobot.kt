package com.squad.tdd.ui.main

import androidx.navigation.NavController
import com.squad.tdd.R
import com.squad.tdd.utils.BaseTestRobot

class MainScreenRobot: BaseTestRobot() {

    fun launchMainScreen(navHostController: NavController) {
        launchFragment(MainFragment(), navHostController)
    }

    fun matchAvatar(drawableId: Int) {
        matchDrawable(R.id.user_avatar, drawableId)
        matchVisible(R.id.user_avatar)
    }

    fun matchName(name: String) = matchText(R.id.user_name, name)

    fun matchEmail(email: String) = matchText(R.id.user_email, email)
}

fun mainScreen(func: MainScreenRobot.() -> Unit) = MainScreenRobot().apply { func() }