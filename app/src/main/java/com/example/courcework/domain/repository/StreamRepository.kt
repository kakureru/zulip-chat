package com.example.courcework.domain.repository

import com.example.courcework.domain.RequestStatus
import com.example.courcework.domain.model.NewStreamParams
import com.example.courcework.domain.model.Stream
import com.example.courcework.domain.model.Topic
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun getStreams(subscribed: Boolean): Flow<List<Stream>>
    fun getStreamTopics(streamId: Int): Flow<List<Topic>>
    suspend fun createNewStream(params: NewStreamParams): RequestStatus
}