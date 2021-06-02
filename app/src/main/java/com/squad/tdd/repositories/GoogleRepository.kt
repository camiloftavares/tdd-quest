package com.squad.tdd.repositories

import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import kotlinx.coroutines.flow.Flow

interface GoogleRepository {
    fun verifyGoogleAccountFlow(idToken: String): Flow<Result<GoogleVerify>>
}
