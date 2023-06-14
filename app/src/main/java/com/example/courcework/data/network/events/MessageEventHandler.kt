package com.example.courcework.data.network.events

import com.example.courcework.data.network.events.model.MessageEvent
import com.example.courcework.domain.model.TopicId
import kotlinx.coroutines.flow.Flow

interface MessageEventHandler {
    fun getEvents(topicId: TopicId): Flow<List<MessageEvent>>
}