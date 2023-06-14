package com.example.courcework.app.di.chat

import com.example.courcework.app.presentation.screens.chat.ChatFragment
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerDialogFragment
import dagger.Component

@ChatScope
@Component(dependencies = [ChatDeps::class], modules = [ChatAppModule::class, ChatDataModule::class, ChatDomainModule::class])
interface ChatComponent {

    fun inject(fragment: ChatFragment)
    fun inject(dialog: TopicPickerDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(chatDeps: ChatDeps): ChatComponent
    }
}