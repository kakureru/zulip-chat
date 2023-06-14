package com.example.courcework.app.di.chat

import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.repository.StreamRepository
import com.example.courcework.domain.usecase.GetTopicsToMoveMessageUseCase
import com.example.courcework.domain.usecase.ReactionClickUseCase
import dagger.Module
import dagger.Provides

@Module
object ChatDomainModule {

    @Provides
    fun provideGetTopicsToMoveMessageUseCase(
        streamRepository: StreamRepository
    ): GetTopicsToMoveMessageUseCase = GetTopicsToMoveMessageUseCase(streamRepository)

    @Provides
    fun provideReactionClickUseCase(
        messageRepository: MessageRepository,
        authHelper: AuthHelper,
    ): ReactionClickUseCase = ReactionClickUseCase(messageRepository, authHelper)
}