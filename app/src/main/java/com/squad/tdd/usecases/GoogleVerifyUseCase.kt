package com.squad.tdd.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.onEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GoogleVerifyUseCase(
    private val googleRepository: GoogleRepository,
    private val userPreference: UserPreference,
    private val logger: AppLogger
) {

    fun verifyGoogleCoroutine(userInfo: UserInfo): Flow<Result<GoogleVerify>> {
        return googleRepository.verifyGoogleAccountFlow(userInfo.idToken)
            .onEach { result ->
                if (result.isApiSuccess) {
                    userPreference.saveUserInfo(userInfo)
                    logger.userSignedIn()
                }
            }
    }
}