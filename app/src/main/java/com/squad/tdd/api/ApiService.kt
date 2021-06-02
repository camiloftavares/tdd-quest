package com.squad.tdd.api

import com.squad.tdd.data.GoogleVerify
import kotlinx.coroutines.flow.Flow

interface ApiService {
    suspend fun verifyGoogle(googleToken: String): Flow<GoogleVerify>

}
