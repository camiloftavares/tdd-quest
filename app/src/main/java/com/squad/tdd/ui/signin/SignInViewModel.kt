package com.squad.tdd.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squad.tdd.data.GoogleVerify
import com.squad.tdd.data.Result
import com.squad.tdd.data.UserInfo
import com.squad.tdd.usecases.GoogleVerifyUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(
    private val googleVerifyUseCase: GoogleVerifyUseCase
) : ViewModel() {

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