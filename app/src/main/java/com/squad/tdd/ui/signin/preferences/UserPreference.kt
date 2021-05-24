package com.squad.tdd.ui.signin.preferences

import com.squad.tdd.ui.signin.data.UserInfo

interface UserPreference {
    fun saveUserInfo(userInfo: UserInfo)
}
