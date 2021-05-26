package com.squad.tdd.preferences

import com.squad.tdd.data.UserInfo

interface UserPreference {
    fun saveUserInfo(userInfo: UserInfo)
}
