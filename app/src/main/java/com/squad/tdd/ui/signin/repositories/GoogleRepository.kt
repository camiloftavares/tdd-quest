package com.squad.tdd.ui.signin.repositories

import androidx.lifecycle.LiveData
import com.squad.tdd.ui.signin.data.GoogleVerify
import com.squad.tdd.ui.signin.data.Result

interface GoogleRepository {
    fun verifyGoogleAccount(googleToken: String): LiveData<Result<GoogleVerify>>

}
