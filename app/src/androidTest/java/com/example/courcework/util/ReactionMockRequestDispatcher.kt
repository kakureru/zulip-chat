package com.example.courcework.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class ReactionMockRequestDispatcher(
    private val messagesRequest: String,
    private val registerRequest: String,
    private val eventsRequest: String,
    private val messageBaseResponse: MockResponse,
    private val messageWithReactionResponse: MockResponse,
    private val registerResponse: MockResponse,
    private val reactionEventResponse: MockResponse,
) : Dispatcher() {

    private var reactionAdded = false
    private val emptyMessageResponse = MockResponse().setBody(loadFromAssets("empty_message_list.json"))
    private val emptyEventResponse = MockResponse().setBody(loadFromAssets("empty_event_list.json"))

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path?.cutParams()) {
            messagesRequest -> {
                if (request.sequenceNumber == 0) messageBaseResponse
                else if (reactionAdded) messageWithReactionResponse
                else emptyMessageResponse
            }
            eventsRequest -> {
                if (reactionAdded) reactionEventResponse
                else emptyEventResponse
            }
            registerRequest -> registerResponse
            else -> MockResponse().setResponseCode(404)
        }
    }

    fun addReaction() {
        reactionAdded = true
    }

    private fun String.cutParams() = substring(0, indexOf('?'))
}