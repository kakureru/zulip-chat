package com.example.courcework.data.network.model.streams

import com.google.gson.annotations.SerializedName

class AllStreamsResponse(
    @SerializedName("streams") val streams: List<StreamDto>
)