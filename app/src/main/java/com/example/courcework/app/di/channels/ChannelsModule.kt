package com.example.courcework.app.di.channels

import androidx.lifecycle.ViewModel
import com.example.courcework.app.di.ViewModelKey
import com.example.courcework.app.presentation.screens.channels.ChannelsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChannelsModule {

    @ChannelsScope
    @Binds
    @IntoMap
    @ViewModelKey(ChannelsViewModel::class)
    fun bindsChannelsViewModel(viewModel: ChannelsViewModel): ViewModel
}