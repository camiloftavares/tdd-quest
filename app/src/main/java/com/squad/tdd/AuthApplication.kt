package com.squad.tdd

import android.app.Application
import android.content.Context
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.preferences.UserPreference

class AuthApplication : Application() {

    val userPreference: UserPreference
        get() = ServiceLocator.provideUserPreference(this)

    val appContext: Context
        get() = this

}