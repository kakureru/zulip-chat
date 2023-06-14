package com.example.courcework.app.di.stream

import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.events.MessageEventHandlerImpl
import com.example.courcework.data.network.events.StreamEventHandler
import com.example.courcework.data.network.events.StreamEventHandlerImpl
import com.example.courcework.data.repository.MessageRepositoryImpl
import com.example.courcework.data.repository.StreamRepositoryImpl
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.repository.StreamRepository
import dagger.Binds
import dagger.Module

@Module
interface StreamDataModule {

    @StreamScope
    @Binds
    fun bindMessageEventHandler(impl: MessageEventHandlerImpl): MessageEventHandler

    @StreamScope
    @Binds
    fun bindStreamEventHandler(impl: StreamEventHandlerImpl): StreamEventHandler

    @StreamScope
    @Binds
    fun bindStreamRepository(impl: StreamRepositoryImpl): StreamRepository

    @StreamScope
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}