package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Topic
import com.example.courcework.domain.model.TopicId
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTopicsToMoveMessageUseCase(private val streamRepository: StreamRepository) {
    operator fun invoke(currentTopicId: TopicId): Flow<List<Topic>> =
        streamRepository.getStreamTopics(currentTopicId.streamId).map { topics ->
            topics.filter { it.name != currentTopicId.topicName }
        }
}