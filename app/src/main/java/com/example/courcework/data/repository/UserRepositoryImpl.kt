package com.example.courcework.data.repository

import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.data.network.model.users.AuthCredentials
import com.example.courcework.domain.model.User
import com.example.courcework.domain.model.toDomain
import com.example.courcework.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authHelper: AuthHelper,
    private val userApi: UserApi
) : UserRepository {

    override suspend fun signIn(authCredentials: AuthCredentials) {
        authHelper.setAuthData(authCredentials)
        userApi.getOwnUser().let {
            authHelper.setUserId(it.id)
            authHelper.setAuthStatus(true)
        }
    }

    override suspend fun getUser(): User =
        userApi.getOwnUser().toDomain(userApi.getUserPresence(authHelper.userId).presence.presence.toDomain())
}