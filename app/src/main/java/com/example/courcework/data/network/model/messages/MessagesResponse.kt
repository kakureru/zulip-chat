package com.example.courcework.data.network.model.messages

import com.google.gson.annotations.SerializedName

class MessagesResponse(
    @SerializedName("messages") val messages: List<MessageDto>
)
