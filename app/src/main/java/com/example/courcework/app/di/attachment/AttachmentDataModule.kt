package com.example.courcework.app.di.attachment

import com.example.courcework.data.network.events.MessageEventHandler
import com.example.courcework.data.network.events.MessageEventHandlerImpl
import com.example.courcework.data.repository.FileRepositoryImpl
import com.example.courcework.data.repository.MessageRepositoryImpl
import com.example.courcework.domain.repository.FileRepository
import com.example.courcework.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module

@Module
interface AttachmentDataModule {

    @AttachmentScope
    @Binds
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository

    @AttachmentScope
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @AttachmentScope
    @Binds
    fun bindMessageEventHandler(impl: MessageEventHandlerImpl): MessageEventHandler
}