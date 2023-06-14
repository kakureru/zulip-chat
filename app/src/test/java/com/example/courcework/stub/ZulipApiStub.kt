package com.example.courcework.stub

import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.events.model.GetEventResponse
import com.example.courcework.data.network.events.model.RegisterEventQueueResponse

class ZulipApiStub : ZulipApi {
    override suspend fun registerQueue(
        allPublicStreams: Boolean,
        eventTypes: String,
        narrow: String
    ): RegisterEventQueueResponse = RegisterEventQueueResponse("0", -1)

    override suspend fun getEvents(queueId: String, id: Int, dontBlock: Boolean): GetEventResponse =
        GetEventResponse(emptyList())
}