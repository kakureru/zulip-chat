package com.example.courcework.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockRequestDispatcher : Dispatcher() {

    private val responses: MutableMap<String, MockResponse> = mutableMapOf()

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.sequenceNumber) {
            0 -> {
                val path = request.path
                responses[path?.substring(0, path.indexOf('?'))] ?: MockResponse().setResponseCode(404)
            }
            else -> MockResponse().setBody(loadFromAssets("empty_message_list.json"))
        }
    }

    fun returnsForPath(path: String, response: MockResponse.() -> MockResponse ) {
        responses[path] = response(MockResponse())
    }
}
