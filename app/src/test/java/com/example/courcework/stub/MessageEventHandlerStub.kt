package com.example.courcework.stub

import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.events.model.MessageEvent
import com.example.courcework.domain.model.TopicId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class MessageEventHandlerStub : MessageEventHandler {
    override fun getEvents(topicId: TopicId): Flow<List<MessageEvent>> = emptyFlow()
}