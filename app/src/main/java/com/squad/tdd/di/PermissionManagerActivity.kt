package com.squad.tdd.di

import com.squad.tdd.helpers.PermissionManager

interface PermissionManagerActivity {
    fun requirePermissionManager(): PermissionManager
}