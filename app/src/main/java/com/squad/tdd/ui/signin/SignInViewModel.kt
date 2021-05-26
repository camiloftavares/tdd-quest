package com.squad.tdd.ui.signin

import androidx.lifecycle.*
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.repositories.GoogleRepository
import com.squad.tdd.usecases.GoogleVerifyUseCase
import com.squad.tdd.utils.AppLogger
import com.squad.tdd.utils.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignInViewModel(
    private val googleVerifyUseCase: GoogleVerifyUseCase
) : ViewModel() {

    fun verifyGoogle(userInfo: UserInfo): LiveData<Result<GoogleVerify>> {
        return googleVerifyUseCase.verifyGoogle(userInfo)
    }

    fun verifyGoogleCoroutine(userInfo: UserInfo): LiveData<Result<GoogleVerify>> {
        val verifyGoogleResult = MutableLiveData<Result<GoogleVerify>>()
        viewModelScope.launch {
            googleVerifyUseCase.verifyGoogleCoroutine(userInfo)
                    .collect {
                        verifyGoogleResult.value = it
            }
        }
        return verifyGoogleResult
    }
}