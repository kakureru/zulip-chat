package com.example.courcework.data.repository

import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.model.MessageEntity
import com.example.courcework.data.db.model.MessageWithReactions
import com.example.courcework.data.db.model.ReactionEntity
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.model.messages.MessageDto
import com.example.courcework.data.network.model.messages.MessagesResponse
import com.example.courcework.data.network.model.messages.ReactionDto
import com.example.courcework.domain.model.TopicId
import com.example.courcework.domain.model.toDomain
import com.example.courcework.stub.MessageApiStub
import com.example.courcework.stub.MessageDaoStub
import com.example.courcework.stub.MessageEventHandlerStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MessageRepositoryImplTest {

    @Test
    fun `getting a list of messages if there is data in database`() = runTest {
        val cache = listOf(
            createMessageWithReactions(1),
            createMessageWithReactions(2),
            createMessageWithReactions(3)
        )
        val messageDao = createMessageDaoStub { cache }
        val repository = createRepository(messageDao = messageDao)

        val messages = repository.getTopicMessages(TopicId(1, "Topic"), 50).first()

        assertEquals(cache.map { it.toDomain() }, messages)
    }

    @Test
    fun `getting a list of messages if there is no data in database`() = runTest {
        val fresh = listOf(
            createMessageDto(1),
            createMessageDto(2),
            createMessageDto(3),
        )
        val messageApi = createMessageApiStub { MessagesResponse(fresh) }
        val repository = createRepository(messageApi = messageApi)

        val messages = repository.getTopicMessages(TopicId(1, "Topic"), 50).first()

        assertEquals(fresh.map { it.toDomain() }, messages)
    }

    @Test
    fun `error loading data from the server`() = runTest {
        val cache = listOf(
            createMessageWithReactions(1),
            createMessageWithReactions(2),
            createMessageWithReactions(3)
        )
        val messageDao = createMessageDaoStub { cache }
        val messageApi = createMessageApiStub { throw Exception() }
        val repository = createRepository(messageDao, messageApi)

        val messages = repository.getTopicMessages(TopicId(1, "Topic"), 50).first()

        assertEquals(cache.map { it.toDomain() }, messages)
    }

    private fun createRepository(
        messageDao: MessageDao = MessageDaoStub(),
        messageApi: MessageApi = MessageApiStub(),
        messageEventHandler: MessageEventHandler = MessageEventHandlerStub()
    ): MessageRepositoryImpl = MessageRepositoryImpl(messageDao, messageApi, messageEventHandler)

    private fun createMessageEntity(
        id: Int,
        topicId: TopicId = TopicId(0, ""),
        senderId: Int = 0,
        author: String = "",
        text: String = "cached message",
        avatarUrl: String = "",
        dateTime: LocalDateTime = LocalDateTime.now()
    ): MessageEntity = MessageEntity(
        id = id,
        topicId = topicId,
        senderId = senderId,
        author = author,
        text = text,
        avatarUrl = avatarUrl,
        dateTime = dateTime
    )

    private fun createMessageDto(
        id: Int,
        streamId: Int = 0,
        senderId: Int = 0,
        name: String = "",
        content: String = "fresh message",
        avatarUrl: String? = "",
        timestamp: Long = 0L,
        reactions: List<ReactionDto> = emptyList()
    ): MessageDto = MessageDto(
        id = id,
        streamId = streamId,
        senderId = senderId,
        name = name,
        content = content,
        avatarUrl = avatarUrl,
        timestamp = timestamp,
        reactions = reactions
    )

    private fun createMessageWithReactions(
        id: Int,
        messageEntity: MessageEntity = createMessageEntity(id),
        reactions: List<ReactionEntity> = emptyList(),
    ): MessageWithReactions = MessageWithReactions(
        message = messageEntity,
        reactions = reactions
    )

    private fun createMessageDaoStub(
        resultProvider: () -> List<MessageWithReactions> = { emptyList() }
    ): MessageDaoStub = MessageDaoStub().apply { this.resultProvider = resultProvider }

    private fun createMessageApiStub(
        resultProvider: () -> MessagesResponse = { MessagesResponse(emptyList()) }
    ): MessageApiStub = MessageApiStub().apply { this.resultProvider = resultProvider }
}