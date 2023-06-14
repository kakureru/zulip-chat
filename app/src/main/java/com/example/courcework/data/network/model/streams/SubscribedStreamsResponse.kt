package com.example.courcework.data.network.model.streams

import com.google.gson.annotations.SerializedName

class SubscribedStreamsResponse (
    @SerializedName("subscriptions") val streams: List<StreamDto>
)