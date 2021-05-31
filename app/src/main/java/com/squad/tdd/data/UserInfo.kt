package com.squad.tdd.data

data class UserInfo(
    val idToken: String,
    val name: String = "",
    val email: String = "",
    val avatar: String = ""
)
