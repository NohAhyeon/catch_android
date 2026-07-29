package com.umc.catchandroid.data.remote

data class LoginRequest(
    val authorizationCode: String,
    val socialType: String
)

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
    val isNewUser: Boolean
)