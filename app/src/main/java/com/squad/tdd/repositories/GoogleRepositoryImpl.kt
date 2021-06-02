package com.squad.tdd.repositories

import com.squad.tdd.api.ApiService
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GoogleRepositoryImpl(private val service: ApiService): GoogleRepository {

    override fun verifyGoogleAccountFlow(idToken: String): Flow<Result<GoogleVerify>> {
        return flow {
            emit(Result.Loading)
            service.verifyGoogle(idToken)
                .collect {
                    if (it.code != "200") {
                        emit(Result.ApiError(it.code))
                    }
                    emit(Result.Success(it))
            }
        }
    }
}