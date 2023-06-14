package com.example.courcework.domain.repository

import com.example.courcework.domain.model.EmojiNCS
import com.example.courcework.domain.model.Message
import com.example.courcework.domain.model.TopicId
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getTopicMessages(topicId: TopicId, pageSize: Int): Flow<List<Message>>
    fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>>
    suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int
    suspend fun getMessage(messageId: Int): Message
    suspend fun sendMessage(topicId: TopicId, message: String)
    suspend fun deleteMessage(messageId: Int)
    suspend fun editMessage(messageId: Int, newContent: String)
    suspend fun changeMessageTopic(messageId: Int, newTopic: String)
    suspend fun addReaction(emoji: EmojiNCS, messageId: Int)
    suspend fun removeReaction(emoji: EmojiNCS, messageId: Int)
    fun getEmojiSet(): List<EmojiNCS>
}