package com.example.courcework.data.network.auth

import com.example.courcework.data.network.model.users.AuthCredentials

interface AuthHelper {

    val isAuthorized: Boolean
    val authHeader: String
    val credentials: String
    val userId: Int

    fun setAuthData(authCredentials: AuthCredentials)
    fun setUserId(userId: Int)
    fun cleanAuthData()
    fun setAuthStatus(isAuthorized: Boolean)
}