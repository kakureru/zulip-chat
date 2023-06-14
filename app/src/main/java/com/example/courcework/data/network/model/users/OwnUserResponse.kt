package com.example.courcework.data.network.model.users

import com.google.gson.annotations.SerializedName

class OwnUserResponse(
    @SerializedName("user_id") val id: Int,
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar_url") val avatarUrl: String?
)