package com.example.courcework.data.network.model.messages

import com.google.gson.annotations.SerializedName

class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("sender_full_name") val name: String,
    @SerializedName("content") val content: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("reactions") val reactions: List<ReactionDto>?,
)