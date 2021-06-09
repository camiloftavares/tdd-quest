package com.squad.tdd.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.helpers.SignInHelperImpl
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.preferences.UserPreferenceImp

object ServiceLocator {

    @Volatile
    var userPreference: UserPreference? = null
        @VisibleForTesting set

    var signInHelper: SignInHelper? = null
        @VisibleForTesting set

    fun provideUserPreference(context: Context): UserPreference {
        return userPreference ?: UserPreferenceImp(context)
    }

    fun provideSignInHelper(): SignInHelper {
        return signInHelper ?: SignInHelperImpl()
    }
}