package com.example.courcework.stub

import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.model.MessageEntity
import com.example.courcework.data.db.model.MessageWithReactions
import com.example.courcework.data.db.model.ReactionEntity
import com.example.courcework.domain.model.TopicId

class MessageDaoStub: MessageDao {

    var resultProvider: () -> List<MessageWithReactions> = { emptyList() }

    override suspend fun getTopicMessages(topicId: TopicId): List<MessageWithReactions> =
        resultProvider()

    override suspend fun clearMessages() = Unit

    override suspend fun saveMessages(messages: List<MessageEntity>) = Unit

    override suspend fun clearReactions() = Unit

    override suspend fun saveReactions(reactions: List<ReactionEntity>) = Unit
}