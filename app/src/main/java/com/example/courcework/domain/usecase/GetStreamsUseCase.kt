package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Stream
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStreamsUseCase(private val streamRepository: StreamRepository) {
    operator fun invoke(onlySubscribed: Boolean): Flow<List<Stream>> {
        return streamRepository.getStreams(onlySubscribed).map { it.sortedBy { stream -> stream.name } }
    }
}