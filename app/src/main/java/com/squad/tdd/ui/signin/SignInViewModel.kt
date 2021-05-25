package com.squad.tdd.ui.signin

import androidx.lifecycle.*
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result
import com.squad.tdd.ui.signin.data.UserInfo
import com.squad.tdd.ui.signin.preferences.UserPreference
import com.squad.tdd.ui.signin.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.onEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignInViewModel(private val googleRepository: GoogleRepository,
                      private val userPreference: UserPreference,
                      private val logger: AppLogger) : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()
    private val _verifyGoogleMutable = MutableLiveData<Result<GoogleVerify>>()

    fun verifyGoogle(userInfo: UserInfo): LiveData<Result<GoogleVerify>> {
        _userInfo.value = userInfo
        return _userInfo.switchMap {
            googleRepository.verifyGoogleAccount(userInfo.idToken)
                .onEach {
                    if (it.isApiSuccess) {
                        userPreference.saveUserInfo(userInfo)
                        logger.userSignedIn()
                }
            }
        }
    }

    fun verifyGoogleCoroutine(userInfo: UserInfo): LiveData<Result<GoogleVerify>> {
        viewModelScope.launch {
            googleRepository.verifyGoogleAccountFlow(userInfo.idToken)
                    .onEach { result ->
                        if (result.isApiSuccess) {
                            userPreference.saveUserInfo(userInfo)
                            logger.userSignedIn()
                        }
                    }
                    .collect {
                        _verifyGoogleMutable.value = it
            }
        }
        return _verifyGoogleMutable
    }
}