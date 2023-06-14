package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Stream
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStreamSearchResultUseCase(private val streamRepository: StreamRepository) {
    operator fun invoke(query: String, searchInSubscribed: Boolean): Flow<List<Stream>> {
        return streamRepository.getStreams(searchInSubscribed).map { streamList ->
            streamList
                .filter { it.name.lowercase().startsWith(query.lowercase()) }
                .sortedBy { stream -> stream.name }
        }
    }
}