package com.squad.tdd.ui.signin

import androidx.lifecycle.*
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result
import com.squad.tdd.ui.signin.data.UserInfo
import com.squad.tdd.ui.signin.preferences.UserPreference
import com.squad.tdd.ui.signin.repositories.GoogleRepository
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.onNext

class SignInViewModel(private val googleRepository: GoogleRepository,
                      private val userPreference: UserPreference,
                      private val logger: AppLogger) : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()

    fun verifyGoogle(userInfo: UserInfo): LiveData<Result<GoogleVerify>> {
        _userInfo.value = userInfo
        return _userInfo.switchMap {
            googleRepository.verifyGoogleAccount(userInfo.idToken)
        }.onNext {
            if (it.isApiSuccess) {
                userPreference.saveUserInfo(userInfo)
                logger.userSignedIn()
            }
        }
    }
}