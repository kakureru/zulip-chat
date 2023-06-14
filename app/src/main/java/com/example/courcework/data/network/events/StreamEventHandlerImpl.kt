package com.example.courcework.data.network.events

import android.util.Log
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.events.model.MessageEvent
import com.example.courcework.data.network.events.model.StreamEvent
import com.example.courcework.domain.model.TopicId
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class StreamEventHandlerImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : StreamEventHandler {

    private var lastEventId: Int = -1
    private var eventQueueId: String? = null
    private val mInterval = 1000L

    private suspend fun register() {
        runCatchingNonCancellation {
            zulipApi.registerQueue(
                allPublicStreams = true,
                eventTypes = """["stream", "subscription"]"""
            ).let {
                eventQueueId = it.queueId
                lastEventId = it.lastEventId
            }
        }.onFailure {
            Log.d("StreamEventHandler", "Registration failed", it)
        }
    }

    override fun getEvents(): Flow<List<StreamEvent>> = flow {
        register()
        while (currentCoroutineContext().isActive) {
            runCatchingNonCancellation {
                eventQueueId?.let { queueId ->
                    val events = zulipApi.getEvents(queueId, lastEventId, true).events
                    if (events.isNotEmpty()) {
                        lastEventId = events.last().id
                        emit(events.map { StreamEvent(it.id) })
                    }
                }
                delay(mInterval)
            }.onFailure {
                Log.d("StreamEventHandler", "Run failed", it)
            }
        }
    }
}