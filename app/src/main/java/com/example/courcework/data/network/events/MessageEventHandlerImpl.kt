package com.example.courcework.data.network.events

import android.util.Log
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.events.model.MessageEvent
import com.example.courcework.domain.model.TopicId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MessageEventHandlerImpl @Inject constructor(
    private val zulipApi: ZulipApi
) : MessageEventHandler {

    private var lastEventId: Int = -1
    private var eventQueueId: String? = null
    private val mInterval = 1000L

    private suspend fun register(topicId: TopicId) {
        runCatchingNonCancellation {
            zulipApi.registerQueue(
                allPublicStreams = true,
                eventTypes = """["message", "delete_message", "update_message", "reaction"]""",
                narrow = """[["topic", "${topicId.topicName}"]]"""
            ).let {
                eventQueueId = it.queueId
                lastEventId = it.lastEventId
            }
        }.onFailure {
            Log.d("StreamEventHandler", "Registration failed", it)
        }
    }

    override fun getEvents(topicId: TopicId): Flow<List<MessageEvent>> = flow {
        register(topicId)
        while (currentCoroutineContext().isActive) {
            runCatchingNonCancellation {
                eventQueueId?.let { queueId ->
                    val events = zulipApi.getEvents(queueId, lastEventId, true).events
                    if (events.isNotEmpty()) {
                        lastEventId = events.last().id
                        emit(events.map { MessageEvent(it.id) })
                    }
                }
                delay(mInterval)
            }.onFailure {
                Log.d("MessageEventHandler", "Run failed", it)
            }
        }
    }
}