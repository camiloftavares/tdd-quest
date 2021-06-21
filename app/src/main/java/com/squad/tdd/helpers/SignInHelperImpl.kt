package com.squad.tdd.helpers

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squad.tdd.BuildConfig
import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking


class SignInHelperImpl(private val activity: FragmentActivity): SignInHelper {

    val channel = Channel<UserInfo>()

    private val loginResultHandler = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        runBlocking {

        Log.d("fasd", "${result.data}")
        }

    }

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
        .requestEmail()
        .build()

    private val googleClient = GoogleSignIn.getClient(activity, gso)


    override fun showSignInIntent() {
        loginResultHandler.launch(googleClient.signInIntent)
    }

    override fun signInResult(): Flow<UserInfo> {
        return flowOf()
    }

    override fun logout() {
        googleClient.signOut()
    }

    override fun userIsLogged(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(activity) != null
    }
}