package com.squad.tdd.data

import java.lang.Exception

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class ApiError(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    val isApiSuccess get() = this is Success
}