package com.squad.tdd.ui.signin

import com.squad.tdd.helpers.PermissionManager

class FakePermissionManager: PermissionManager {

    var isPermissionDenied: Boolean = false

    override fun requestPermission(
        requestedPermissions: String,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {

    }

    override fun isPermissionGranted(requestedPermission: String): Boolean {
        return false
    }

    override fun handlePermission(
        requestedPermission: String,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        rationaleTextId: Int
    ) {
        if (isPermissionDenied) {
            onPermissionDenied()
        } else {
            onPermissionGranted()
        }
    }
}