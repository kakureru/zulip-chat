package com.example.courcework.stub

import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.data.network.model.users.AuthCredentials

class AuthHelperStub : AuthHelper {
    override val isAuthorized: Boolean = true
    override val authHeader: String = "Authorization"
    override val credentials: String = "Fake credentials"
    override val userId: Int = 0

    override fun setAuthData(authCredentials: AuthCredentials) = Unit
    override fun setUserId(userId: Int) = Unit
    override fun cleanAuthData() = Unit
    override fun setAuthStatus(isAuthorized: Boolean) = Unit
}