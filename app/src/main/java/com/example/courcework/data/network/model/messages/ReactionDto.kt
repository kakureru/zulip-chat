package com.example.courcework.data.network.model.messages

import com.google.gson.annotations.SerializedName

class ReactionDto(
    @SerializedName("emoji_name") val emojiName: String,
    @SerializedName("emoji_code") val emojiCode: String,
    @SerializedName("user_id") val userId: Int,
)