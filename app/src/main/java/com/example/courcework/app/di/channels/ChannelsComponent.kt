package com.example.courcework.app.di.channels

import com.example.courcework.app.presentation.screens.channels.ChannelsFragment
import dagger.Component

@ChannelsScope
@Component(dependencies = [ChannelsDeps::class], modules = [ChannelsModule::class])
interface ChannelsComponent {

    fun inject(fragment: ChannelsFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: ChannelsDeps): ChannelsComponent
    }
}