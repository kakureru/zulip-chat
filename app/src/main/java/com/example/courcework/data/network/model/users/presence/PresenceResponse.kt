package com.example.courcework.data.network.model.users.presence

import com.google.gson.annotations.SerializedName

class PresenceResponse(
    @SerializedName("presence") val presence: PresenceDto,
)