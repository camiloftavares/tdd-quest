package com.squad.tdd.helpers

import android.content.IntentSender.SendIntentException
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squad.tdd.BuildConfig


class SignInHelperImpl(private val activity: FragmentActivity): SignInHelper, DefaultLifecycleObserver {
    val loginResultHandler = activity.registerForActivityResult(StartIntentSenderForResult()) { result ->
            Log.d("fasd", "${result.resultCode}")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun showSignInIntent() {
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
            .build()
        Identity.getSignInClient(activity)
            .getSignInIntent(request)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(activity, result.intentSender, 1, null, 0,0,0,null)
                } catch (e: SendIntentException) {
                    Log.e("error", e.toString())
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e("error", e.toString())
            }
    }

    override fun userIsLogged(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(activity) != null
    }
}