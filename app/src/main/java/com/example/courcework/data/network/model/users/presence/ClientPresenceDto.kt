package com.example.courcework.data.network.model.users.presence

import com.google.gson.annotations.SerializedName

class ClientPresenceDto(
    @SerializedName("status") val status: String,
    @SerializedName("timestamp") val timestamp: Int
)