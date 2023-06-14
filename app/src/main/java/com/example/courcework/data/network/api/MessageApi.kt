package com.example.courcework.data.network.api

import com.example.courcework.data.network.model.messages.FetchMessageResponse
import com.example.courcework.data.network.model.messages.MessagesResponse
import com.example.courcework.data.network.model.messages.SendMessageResponse
import retrofit2.http.*

interface MessageApi {

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String,
        @Query("apply_markdown") applyMarkdown: Boolean = true
    ): MessagesResponse

    @FormUrlEncoded
    @POST("messages")
    suspend fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: Int,
        @Field("topic") topic: String,
        @Field("content") content: String
    ): SendMessageResponse

    @DELETE("messages/{message_id}")
    suspend fun deleteMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
    )

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    suspend fun editMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("content") content: String,
    )

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    suspend fun changeMessageTopic(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("topic") newTopic: String,
    )

    @GET("messages/{message_id}")
    suspend fun fetchMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Query("apply_markdown") applyMarkdown: Boolean = true
    ): FetchMessageResponse

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("emoji_name") emojiName: String
    )

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "messages/{message_id}/reactions", hasBody = true)
    suspend fun removeReaction(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("emoji_name") emojiName: String
    )
}