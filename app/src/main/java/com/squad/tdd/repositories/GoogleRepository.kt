package com.squad.tdd.repositories

import androidx.lifecycle.LiveData
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import kotlinx.coroutines.flow.Flow

interface GoogleRepository {
    fun verifyGoogleAccount(googleToken: String): LiveData<Result<GoogleVerify>>
    fun verifyGoogleAccountFlow(idToken: String): Flow<Result<GoogleVerify>>

}
