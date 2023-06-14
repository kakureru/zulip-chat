package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Topic
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class GetTopicsUseCase(
    private val streamRepository: StreamRepository,
    private val messageRepository: MessageRepository
) {
    operator fun invoke(streamId: Int): Flow<List<Topic>> = channelFlow {
        val currentData = mutableListOf<Topic>()
        streamRepository.getStreamTopics(streamId).collect { newTopicsNoCount ->
            val newTopicsOldCount = newTopicsNoCount.setMessageCount(from = currentData)
            currentData.apply {
                clear()
                addAll(newTopicsOldCount)
                send(this)
            }
            newTopicsOldCount.forEachIndexed { index, topic ->
                launch {
                    val count: Int = messageRepository.getTopicUnreadMessageCount(topic.id)
                    currentData[index] = currentData[index].copy(unreadMessageCount = count)
                    send(currentData)
                }
            }
        }
    }

    private fun List<Topic>.setMessageCount(from: List<Topic>) = map { topic ->
        topic.copy(unreadMessageCount = from.firstOrNull { it.id == topic.id }?.unreadMessageCount)
    }
}