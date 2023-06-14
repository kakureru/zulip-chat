package com.example.courcework.data.network.model.users.presence

import com.google.gson.annotations.SerializedName

class PresenceDto(
    @SerializedName("aggregated") val presence: ClientPresenceDto
)