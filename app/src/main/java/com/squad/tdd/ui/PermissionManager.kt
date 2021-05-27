package com.squad.tdd.ui

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

interface PermissionManager {

    fun requestPermission(requestedPermissions: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {})

    fun isPermissionGranted(requestedPermission: String): Boolean

    fun callPermissionBoundedAction(requestedPermission: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {})
}

class PermissionManagerImpl(
    private val activity: Activity,
    private val permissionLauncher: (requestedPermissions: String, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) -> Unit
) : PermissionManager {

    private fun checkPermissionStatus(requestedPermission: String): Int {
        return ContextCompat.checkSelfPermission(activity.applicationContext, requestedPermission)
    }

    override fun requestPermission(requestedPermissions: String, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        permissionLauncher(requestedPermissions, onPermissionGranted, onPermissionDenied)
    }

    override fun isPermissionGranted(requestedPermission: String): Boolean {
        return checkPermissionStatus(requestedPermission) == PackageManager.PERMISSION_GRANTED
    }

    override fun callPermissionBoundedAction(requestedPermission: String, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        when (checkPermissionStatus(requestedPermission)) {
            PackageManager.PERMISSION_GRANTED -> onPermissionGranted()
            else -> requestPermission(requestedPermission, onPermissionGranted, onPermissionDenied)
        }
    }

}