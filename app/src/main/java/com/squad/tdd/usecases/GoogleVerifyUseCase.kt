package com.squad.tdd.usecases

import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.flow.Flow

interface GoogleVerifyUseCase {
    fun verifyGoogleCoroutine(userInfo: UserInfo): Flow<Result<GoogleVerify>>
}