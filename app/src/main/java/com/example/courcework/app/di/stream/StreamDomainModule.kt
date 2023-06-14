package com.example.courcework.app.di.stream

import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.repository.StreamRepository
import com.example.courcework.domain.usecase.GetStreamSearchResultUseCase
import com.example.courcework.domain.usecase.GetStreamsUseCase
import com.example.courcework.domain.usecase.GetTopicsUseCase
import dagger.Module
import dagger.Provides

@Module
object StreamDomainModule {

    @Provides
    fun provideGetTopicsUseCase(
        streamRepository: StreamRepository,
        messageRepository: MessageRepository
    ): GetTopicsUseCase = GetTopicsUseCase(streamRepository, messageRepository)

    @Provides
    fun provideGetStreamsUseCase(
        streamRepository: StreamRepository
    ): GetStreamsUseCase = GetStreamsUseCase(streamRepository)

    @Provides
    fun provideGetStreamSearchResultUseCase(
        streamRepository: StreamRepository
    ): GetStreamSearchResultUseCase = GetStreamSearchResultUseCase(streamRepository)
}