package com.squad.tdd.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHelperImpl(val activity: Activity) : PermissionHelper {

    companion object {
        const val LOCATION_REQUEST_CODE = 10
    }

    override fun isLocationPermissionGranted(): Boolean {
        val checkSelfPermission =
            ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun requestLocationPermission() {
        activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
    }
}