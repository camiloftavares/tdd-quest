package com.squad.tdd.helpers

import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.flow.Flow

interface SignInHelper {
    fun userIsLogged(): Boolean
    fun showSignInIntent()
    fun signInResult(): Flow<UserInfo>
    fun logout()
}
