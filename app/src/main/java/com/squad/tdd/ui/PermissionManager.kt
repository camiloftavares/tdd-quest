package com.squad.tdd.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.squad.tdd.R

interface PermissionManager {

    fun requestPermission(requestedPermissions: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {})

    fun isPermissionGranted(requestedPermission: String): Boolean

    fun callPermissionBoundedAction(requestedPermission: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {}, rationaleTextId: Int = R.string.permission_rationale_default_text)
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

    override fun callPermissionBoundedAction(requestedPermission: String, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit, rationaleTextId: Int) {
        when {
            checkPermissionStatus(requestedPermission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            activity.shouldShowRequestPermissionRationale(requestedPermission) -> {
                requestPermissionRationale(rationaleTextId)
            }
            else -> {
                requestPermission(requestedPermission, onPermissionGranted, onPermissionDenied)
            }
        }
    }

    private fun requestPermissionRationale(rationaleTextId: Int) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(activity.getString(rationaleTextId))
            builder.setPositiveButton("OK", { dialog, which ->  })
            builder.show()
        }
    }
}