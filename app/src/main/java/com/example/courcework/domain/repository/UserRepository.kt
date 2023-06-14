package com.example.courcework.domain.repository

import com.example.courcework.data.network.model.users.AuthCredentials
import com.example.courcework.domain.model.User

interface UserRepository {
    suspend fun signIn(authCredentials: AuthCredentials)
    suspend fun getUser(): User
}