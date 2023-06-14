package com.example.courcework.data.network.api

import com.example.courcework.data.network.model.users.AllUsersResponse
import com.example.courcework.data.network.model.users.OwnUserResponse
import com.example.courcework.data.network.model.users.UserResponse
import com.example.courcework.data.network.model.users.presence.AllPresenceResponse
import com.example.courcework.data.network.model.users.presence.PresenceResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users")
    suspend fun getAllUsers(): AllUsersResponse

    @GET("users/{user_id}")
    suspend fun getUser(
        @Path(value = "user_id", encoded = true) userId: Int
    ): UserResponse

    @GET("users/me")
    suspend fun getOwnUser(): OwnUserResponse

    @GET("realm/presence")
    suspend fun getAllPresence(): AllPresenceResponse

    @GET("users/{user_id_or_email}/presence")
    suspend fun getUserPresence(
        @Path(value = "user_id_or_email", encoded = true) userId: Int
    ): PresenceResponse
}