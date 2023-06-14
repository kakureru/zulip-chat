package com.example.courcework.stub

import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.model.messages.FetchMessageResponse
import com.example.courcework.data.network.model.messages.MessagesResponse
import com.example.courcework.data.network.model.messages.SendMessageResponse

class MessageApiStub: MessageApi {

    var resultProvider: () -> MessagesResponse = { MessagesResponse(emptyList()) }

    override suspend fun getMessages(anchor: String, numBefore: Int, numAfter: Int, narrow: String, applyMarkdown: Boolean): MessagesResponse =
        resultProvider()

    override suspend fun sendMessage(type: String, to: Int, topic: String, content: String): SendMessageResponse =
        TODO("Not yet implemented")

    override suspend fun deleteMessage(messageId: Int) = Unit

    override suspend fun editMessage(messageId: Int, content: String) = Unit

    override suspend fun changeMessageTopic(messageId: Int, newTopic: String) = Unit

    override suspend fun fetchMessage(messageId: Int, applyMarkdown: Boolean): FetchMessageResponse =
        TODO("Not yet implemented")

    override suspend fun addReaction(messageId: Int, emojiName: String) = Unit

    override suspend fun removeReaction(messageId: Int, emojiName: String) = Unit
}