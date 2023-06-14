package com.example.courcework.stub

import com.example.courcework.domain.RequestStatus
import com.example.courcework.domain.model.NewStreamParams
import com.example.courcework.domain.model.Stream
import com.example.courcework.domain.model.Topic
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class StreamRepositoryStub : StreamRepository {
    override fun getStreams(subscribed: Boolean): Flow<List<Stream>> = emptyFlow()
    override fun getStreamTopics(streamId: Int): Flow<List<Topic>> = emptyFlow()
    override suspend fun createNewStream(params: NewStreamParams): RequestStatus = RequestStatus.Success
}