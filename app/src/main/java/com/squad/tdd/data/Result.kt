package com.squad.tdd.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class ApiError(val code: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    val isApiSuccess get() = this is Success
}