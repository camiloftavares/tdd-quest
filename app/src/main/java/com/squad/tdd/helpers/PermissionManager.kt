package com.squad.tdd.helpers

import com.squad.tdd.R

interface PermissionManager {

    fun requestPermission(requestedPermissions: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {})

    fun isPermissionGranted(requestedPermission: String): Boolean

    fun handlePermission(requestedPermission: String, onPermissionGranted: () -> Unit = {}, onPermissionDenied: () -> Unit = {}, rationaleTextId: Int = R.string.permission_rationale_default_text)
}