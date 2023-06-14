package com.example.courcework.data.repository

import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.model.MessageEntity
import com.example.courcework.data.db.model.ReactionEntity
import com.example.courcework.data.emojiSet
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.model.messages.Narrow
import com.example.courcework.domain.model.EmojiNCS
import com.example.courcework.domain.model.Message
import com.example.courcework.domain.model.Reaction
import com.example.courcework.domain.model.TopicId
import com.example.courcework.domain.model.toDomain
import com.example.courcework.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageApi: MessageApi,
    private val messageEventHandler: MessageEventHandler
) : MessageRepository {

    private val inMemoryCache = mutableListOf<Message>()
    private var requestInProgress = false
    private var nextId: Int? = null

    companion object {
        private const val NEWEST_ANCHOR = "newest"
        private const val FIRST_UNREAD_ANCHOR = "first_unread"
        private const val CACHE_SIZE = 50
    }

    override fun getEmojiSet(): List<EmojiNCS> = emojiSet

    override fun getTopicMessages(topicId: TopicId, pageSize: Int): Flow<List<Message>> = flow {
        requestInProgress = true
        getCacheMessages(topicId).let {
            if (it.isNotEmpty()) emit(it)
        }
        getFreshMessages(topicId, NEWEST_ANCHOR, pageSize).let {
            inMemoryCache.apply {
                clear()
                addAll(it)
                emit(this)
            }
            requestInProgress = false
            refreshMessages(it, topicId)
        }
        messageEventHandler.getEvents(topicId).collect {
            reloadMessages(topicId)
        }
    }

    override fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>> = flow {
        if (!requestInProgress) {
            requestInProgress = true
            inMemoryCache.addAll(0, getFreshMessages(topicId, nextId.toString(), pageSize))
            emit(inMemoryCache)
            requestInProgress = false
        }
    }

    private suspend fun getCacheMessages(topicId: TopicId): List<Message> =
        messageDao.getTopicMessages(topicId).map { it.toDomain() }

    private suspend fun getFreshMessages(topicId: TopicId, anchor: String, numBefore: Int): List<Message> =
        messageApi.getMessages(
            anchor = anchor,
            numBefore = numBefore,
            numAfter = 0,
            narrow = Narrow.constructForTopic(topicId)
        ).messages
            .map { it.toDomain() }
            .also { if (it.isNotEmpty()) nextId = it.first().id - 1 }

    private suspend fun refreshMessages(freshMessages: List<Message>, topicId: TopicId) =
        freshMessages.take(CACHE_SIZE).let { messages ->
            messageDao.refreshMessages(
                messages = messages.map { it.toEntity(topicId) },
                reactions = messages.flatMap { msg -> msg.reactions.map { it.toEntity(msg.id) } }
            )
        }

    private suspend fun FlowCollector<List<Message>>.reloadMessages(topicId: TopicId) {
        val new = getFreshMessages(topicId, NEWEST_ANCHOR, inMemoryCache.size)
        inMemoryCache.apply {
            clear()
            addAll(new)
            emit(this)
        }
        refreshMessages(new, topicId)
    }

    override suspend fun getMessage(messageId: Int): Message =
        messageApi.fetchMessage(messageId).message.toDomain()

    override suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int =
        messageApi.getMessages(
            anchor = FIRST_UNREAD_ANCHOR,
            numBefore = 0,
            numAfter = 5000,
            narrow = Narrow.constructForTopic(topicId)
        ).messages.size


    override suspend fun sendMessage(topicId: TopicId, message: String) {
        messageApi.sendMessage(
            type = "stream",
            to = topicId.streamId,
            topic = topicId.topicName,
            content = message
        )
    }

    override suspend fun deleteMessage(messageId: Int) {
        messageApi.deleteMessage(messageId)
    }

    override suspend fun editMessage(messageId: Int, newContent: String) {
        messageApi.editMessage(
            messageId = messageId,
            content = newContent,
        )
    }

    override suspend fun changeMessageTopic(messageId: Int, newTopic: String) {
        messageApi.changeMessageTopic(
            messageId = messageId,
            newTopic = newTopic
        )
    }

    override suspend fun addReaction(emoji: EmojiNCS, messageId: Int) {
        messageApi.addReaction(
            messageId = messageId,
            emojiName = emoji.name
        )
    }

    override suspend fun removeReaction(emoji: EmojiNCS, messageId: Int) {
        messageApi.removeReaction(
            messageId = messageId,
            emojiName = emoji.name
        )
    }

    private fun Message.toEntity(topicId: TopicId) = MessageEntity(
        id = id,
        topicId = topicId,
        senderId = senderId,
        author = author,
        text = text,
        avatarUrl = avatarUrl,
        dateTime = dateTime
    )

    private fun Reaction.toEntity(messageId: Int) = ReactionEntity(
        id = "$messageId${emoji.code}$userId",
        messageId = messageId,
        emojiName = emoji.name,
        emojiCode = emoji.code,
        userId = userId,
    )
}