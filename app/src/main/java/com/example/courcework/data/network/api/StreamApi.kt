package com.example.courcework.data.network.api

import com.example.courcework.data.network.model.BaseResponse
import com.example.courcework.data.network.model.streams.AllStreamsResponse
import com.example.courcework.data.network.model.streams.StreamTopicsResponse
import com.example.courcework.data.network.model.streams.SubscribedStreamsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StreamApi {

    @GET("streams")
    suspend fun getAllStreams(): AllStreamsResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getStreamTopics(
        @Path(value = "stream_id", encoded = true) streamId: Int
    ): StreamTopicsResponse

    @FormUrlEncoded
    @POST("users/me/subscriptions")
    suspend fun createStream(
        @Field("subscriptions") subscriptions: String,
        @Field("announce") announce: Boolean,
    ) : Response<BaseResponse>
}