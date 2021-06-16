package com.squad.tdd.di

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.squad.tdd.api.ApiServiceImpl
import com.squad.tdd.helpers.PermissionManager
import com.squad.tdd.helpers.PermissionManagerImpl
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.helpers.SignInHelperImpl
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.preferences.UserPreferenceImp
import com.squad.tdd.repositories.GoogleRepositoryImpl
import com.squad.tdd.usecases.GoogleVerifyUseCase
import com.squad.tdd.usecases.GoogleVerifyUseCaseImpl
import com.squad.tdd.utils.AppLoggerImpl

object ServiceLocator {

    @Volatile
    var userPreference: UserPreference? = null
        @VisibleForTesting set

    var signInHelper: SignInHelper? = null
        @VisibleForTesting set

    var googleVerifyUseCase: GoogleVerifyUseCase? = null
        @VisibleForTesting set

    var permissionManager: PermissionManager? = null
        @VisibleForTesting set

    fun provideUserPreference(context: Context): UserPreference {
        return userPreference ?: UserPreferenceImp(context)
    }

    fun provideSignInHelper(activity: FragmentActivity): SignInHelper {
        return signInHelper ?: SignInHelperImpl(activity)
    }

    fun provideGoogleVerifyUseCase(context: Context): GoogleVerifyUseCase {
        return googleVerifyUseCase ?: GoogleVerifyUseCaseImpl(GoogleRepositoryImpl(ApiServiceImpl()), provideUserPreference(context), AppLoggerImpl())
    }

    fun providePermissionManager(activity: Activity, permissionLauncher: (requestedPermissions: String, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) -> Unit): PermissionManager {
        return permissionManager ?: PermissionManagerImpl(activity, permissionLauncher)
    }

}
fun Activity.requirePermissionManager(): PermissionManager {
    return if (this is PermissionManagerActivity)
        this.requirePermissionManager()
    else
        ServiceLocator.permissionManager!!
}

