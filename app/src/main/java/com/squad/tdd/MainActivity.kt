package com.squad.tdd

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squad.tdd.helpers.PermissionManager
import com.squad.tdd.helpers.PermissionManagerImpl

class MainActivity : AppCompatActivity() {


    private var onPermissionGranted: () -> Unit = {}
    private var onPermissionDenied: () -> Unit = {}

    private val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    private fun launchPermissionRequest(requestedPermissions: String, onPermissionGranted: () -> Unit , onPermissionDenied: () -> Unit)
    {
        this.onPermissionDenied = onPermissionDenied
        this.onPermissionGranted = onPermissionGranted
        permissionResultLauncher.launch(requestedPermissions)
    }

    val permissionManager: PermissionManager = PermissionManagerImpl(this, ::launchPermissionRequest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }



}