package com.example.courcework.data.network.model.messages

import com.google.gson.annotations.SerializedName

class FetchMessageResponse (
    @SerializedName("message") val message: MessageDto
)