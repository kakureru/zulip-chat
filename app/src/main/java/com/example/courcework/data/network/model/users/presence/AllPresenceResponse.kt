package com.example.courcework.data.network.model.users.presence

import com.google.gson.annotations.SerializedName

class AllPresenceResponse(
    @SerializedName("presences") val presences: Map<String, PresenceDto>,
    @SerializedName("server_timestamp") val serverTimestamp: Float
)