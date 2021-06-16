package com.squad.tdd.utils

import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.lang.Exception

class FakeUserPreference: UserPreference {

    var userInfo = UserInfo("idToken", "Caadsamilo", "camilo@gmail.com")
    var isUserLogged = false

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        this.userInfo = userInfo
    }

    override fun getUserInfo(): Flow<UserInfo> = if (isUserLogged) flowOf(userInfo) else throw Exception("Invalid Token ID.")
}