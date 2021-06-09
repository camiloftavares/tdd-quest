package com.squad.tdd.preferences

import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeAndroidUserPreference : UserPreference {
    private val _userInfo = MutableStateFlow(UserInfo(""))

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        _userInfo.value = userInfo
    }

    override fun getUserInfo(): Flow<UserInfo> {
        return flowOf(_userInfo.value)
    }
}