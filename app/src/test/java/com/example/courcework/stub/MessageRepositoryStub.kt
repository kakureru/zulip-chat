package com.example.courcework.stub

import com.example.courcework.domain.model.EmojiNCS
import com.example.courcework.domain.model.Message
import com.example.courcework.domain.model.TopicId
import com.example.courcework.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class MessageRepositoryStub : MessageRepository {
    override fun getTopicMessages(topicId: TopicId, pageSize: Int): Flow<List<Message>> = emptyFlow()
    override fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>> = emptyFlow()
    override suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int = 0
    override suspend fun sendMessage(topicId: TopicId, message: String) = Unit
    override suspend fun deleteMessage(messageId: Int) = Unit
    override suspend fun editMessage(messageId: Int, newContent: String) = Unit
    override suspend fun changeMessageTopic(messageId: Int, newTopic: String) = Unit
    override suspend fun addReaction(emoji: EmojiNCS, messageId: Int) = Unit
    override suspend fun removeReaction(emoji: EmojiNCS, messageId: Int) = Unit
    override fun getEmojiSet(): List<EmojiNCS> = emptyList()
}