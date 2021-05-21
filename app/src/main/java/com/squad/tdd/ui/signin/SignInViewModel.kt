package com.squad.tdd.ui.signin

import androidx.lifecycle.*
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result
import com.squad.tdd.ui.signin.repositories.GoogleRepository

class SignInViewModel(private val googleRepository: GoogleRepository) : ViewModel() {

    private val _googleToken = MutableLiveData<String>()

    fun verifyGoogle(token: String): LiveData<Result<GoogleVerify>> {
        _googleToken.value = token
        return _googleToken.switchMap {
            googleRepository.verifyGoogleAccount(token)
        }
    }
}