package com.example.courcework.data.network.model.streams

import com.google.gson.annotations.SerializedName

class StreamTopicsResponse(
    @SerializedName("topics") val topics: List<TopicDto>
)