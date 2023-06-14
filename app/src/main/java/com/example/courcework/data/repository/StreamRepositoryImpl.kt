package com.example.courcework.data.repository

import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.db.model.StreamEntity
import com.example.courcework.data.db.model.SubscribedStreamEntity
import com.example.courcework.data.db.model.TopicEntity
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.events.StreamEventHandler
import com.example.courcework.domain.RequestStatus
import com.example.courcework.domain.model.NewStreamParams
import com.example.courcework.domain.model.Stream
import com.example.courcework.domain.model.Topic
import com.example.courcework.domain.model.toDomain
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StreamRepositoryImpl @Inject constructor(
    private val streamDao: StreamDao,
    private val streamApi: StreamApi,
    private val streamEventHandler: StreamEventHandler,
) : StreamRepository {

    override fun getStreams(subscribed: Boolean): Flow<List<Stream>> = flow {
        getCacheStreams(subscribed).let { cache ->
            if (cache.isNotEmpty()) emit(cache)
        }
        getFreshStreams(subscribed).let { fresh ->
            emit(fresh)
            refreshStreams(fresh, subscribed)
        }
        streamEventHandler.getEvents().collect {
            reloadStreams(subscribed)
        }
    }

    private suspend fun getCacheStreams(subscribed: Boolean) =
        if (subscribed)
            streamDao.getSubscribedStreams().map { it.toDomain() }
        else
            streamDao.getAllStreams().map { it.toDomain() }

    private suspend fun getFreshStreams(subscribed: Boolean) =
        if (subscribed)
            streamApi.getSubscribedStreams().streams.map { it.toDomain() }
        else
            streamApi.getAllStreams().streams.map { it.toDomain() }

    private suspend fun refreshStreams(streams: List<Stream>, subscribed: Boolean) =
        if (subscribed)
            streamDao.refreshSubscribedStreams(streams.map { it.toSubscribedEntity() })
        else
            streamDao.refreshAllStreams(streams.map { it.toEntity() })

    private suspend fun FlowCollector<List<Stream>>.reloadStreams(subscribed: Boolean) {
        getFreshStreams(subscribed).let { fresh ->
            emit(fresh)
            refreshStreams(fresh, subscribed)
        }
    }

    override fun getStreamTopics(streamId: Int): Flow<List<Topic>> = flow {
        val cache = getCacheTopics(streamId)
        if (cache.isNotEmpty())
            emit(cache)
        val fresh = getFreshTopics(streamId)
        emit(fresh)
        refreshTopics(fresh, streamId)
    }

    private suspend fun getCacheTopics(streamId: Int) =
        streamDao.getTopics(streamId).map { it.toDomain() }

    private suspend fun getFreshTopics(streamId: Int) =
        streamApi.getStreamTopics(streamId).topics.map { it.toDomain(streamId) }

    private suspend fun refreshTopics(topics: List<Topic>, streamId: Int) =
        streamDao.refreshTopics(topics.map { it.toEntity() }, streamId)

    override suspend fun createNewStream(params: NewStreamParams): RequestStatus {
        val subscriptions = """[{"name": "${params.name}", "description": "${params.description}"}]"""
        return streamApi.createStream(
            subscriptions = subscriptions,
            announce = params.announce,
        ).let {
            if (it.isSuccessful) RequestStatus.Success
            else RequestStatus.Failure
        }
    }

    private fun Stream.toEntity() = StreamEntity(
        id = id,
        name = name
    )

    private fun Stream.toSubscribedEntity() = SubscribedStreamEntity(
        id = id,
        name = name
    )

    private fun Topic.toEntity() = TopicEntity(
        id = id,
        streamId = id.streamId,
        name = id.topicName,
    )
}