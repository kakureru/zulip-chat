package com.example.courcework.data.network.api

import com.example.courcework.data.network.events.model.GetEventResponse
import com.example.courcework.data.network.events.model.RegisterEventQueueResponse
import retrofit2.http.*

interface ZulipApi {

    @POST("register")
    suspend fun registerQueue(
        @Query("all_public_streams") allPublicStreams: Boolean,
        @Query("event_types") eventTypes: String,
        @Query("narrow") narrow: String = "[]"
    ): RegisterEventQueueResponse

    @GET("events")
    suspend fun getEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") id: Int,
        @Query("dont_block") dontBlock: Boolean,
    ): GetEventResponse

}