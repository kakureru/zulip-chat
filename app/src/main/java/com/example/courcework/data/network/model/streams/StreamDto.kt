package com.example.courcework.data.network.model.streams

import com.google.gson.annotations.SerializedName

class StreamDto(
    @SerializedName("stream_id") val id: Int,
    @SerializedName("name") val name: String
)