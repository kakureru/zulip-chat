package com.example.courcework.data.network.events.model

import com.google.gson.annotations.SerializedName

class GetEventResponse(
    @SerializedName("events") val events: List<EventDto>
)