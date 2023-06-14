package com.example.courcework.app.di.stream

import com.example.courcework.app.presentation.screens.channels.stream.StreamsFragment
import dagger.Component

@StreamScope
@Component(dependencies = [StreamDeps::class], modules = [StreamDataModule::class, StreamDomainModule::class])
interface StreamComponent {

    fun inject(fragment: StreamsFragment)

    @Component.Factory
    interface Factory {
        fun create(streamDeps: StreamDeps): StreamComponent
    }
}