package com.squad.tdd.preferences

import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserPreference {
    suspend fun saveUserInfo(userInfo: UserInfo)
    fun getUserInfo(): Flow<UserInfo>
}
