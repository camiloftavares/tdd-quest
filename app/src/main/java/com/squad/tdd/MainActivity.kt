package com.squad.tdd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.squad.tdd.di.PermissionManagerActivity
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.helpers.PermissionManager
import com.squad.tdd.helpers.PermissionManagerImpl

class MainActivity : AppCompatActivity(), PermissionManagerActivity {


    private var onPermissionGranted: () -> Unit = {}
    private var onPermissionDenied: () -> Unit = {}

    private val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    private fun launchPermissionRequest(
        requestedPermissions: String,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    )
    {
        this.onPermissionDenied = onPermissionDenied
        this.onPermissionGranted = onPermissionGranted
        permissionResultLauncher.launch(requestedPermissions)
    }

    val permissionManager: PermissionManager
        get() = ServiceLocator.providePermissionManager(this, ::launchPermissionRequest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun requirePermissionManager(): PermissionManager {
        return PermissionManagerImpl(this, ::launchPermissionRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("error", data.toString())
        val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        completedTask.getResult(ApiException::class.java)

        completedTask.result?.email
    }
}