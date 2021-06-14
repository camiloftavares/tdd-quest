package com.squad.tdd.api

import com.squad.tdd.data.GoogleVerify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiServiceImpl: ApiService {
    override suspend fun verifyGoogle(googleToken: String): Flow<GoogleVerify> {
        return flow{}
    }
}