package com.squad.tdd.ui.signin

import androidx.lifecycle.*
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

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory(
    private val googleVerifyUseCase: GoogleVerifyUseCase
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(googleVerifyUseCase) as T
    }
}