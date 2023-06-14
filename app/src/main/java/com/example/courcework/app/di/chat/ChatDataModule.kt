package com.example.courcework.app.di.chat

import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.events.MessageEventHandlerImpl
import com.example.courcework.data.network.events.StreamEventHandler
import com.example.courcework.data.network.events.StreamEventHandlerImpl
import com.example.courcework.data.repository.FileRepositoryImpl
import com.example.courcework.data.repository.MessageRepositoryImpl
import com.example.courcework.data.repository.StreamRepositoryImpl
import com.example.courcework.domain.repository.FileRepository
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.repository.StreamRepository
import dagger.Binds
import dagger.Module

@Module
interface ChatDataModule {

    @ChatScope
    @Binds
    fun bindMessageEventHandler(impl: MessageEventHandlerImpl): MessageEventHandler

    @ChatScope
    @Binds
    fun bindStreamEventHandler(impl: StreamEventHandlerImpl): StreamEventHandler

    @ChatScope
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @ChatScope
    @Binds
    fun bindStreamRepository(impl: StreamRepositoryImpl): StreamRepository

    @ChatScope
    @Binds
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}
