package com.squad.tdd.helpers

import android.content.Context

interface PermissionHelper {
    fun isLocationPermissionGranted(): Boolean

    fun requestLocationPermission()
}