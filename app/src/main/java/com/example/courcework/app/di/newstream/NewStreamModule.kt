package com.example.courcework.app.di.newstream

import androidx.lifecycle.ViewModel
import com.example.courcework.app.di.ViewModelKey
import com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog.NewStreamViewModel
import com.example.courcework.data.network.events.StreamEventHandler
import com.example.courcework.data.network.events.StreamEventHandlerImpl
import com.example.courcework.data.repository.StreamRepositoryImpl
import com.example.courcework.domain.repository.StreamRepository
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface NewStreamModule {

    @NewStreamScope
    @Binds
    @IntoMap
    @ViewModelKey(NewStreamViewModel::class)
    fun bindsNewStreamViewModel(viewModel: NewStreamViewModel): ViewModel

    @NewStreamScope
    @Binds
    fun bindStreamRepository(impl: StreamRepositoryImpl): StreamRepository

    @NewStreamScope
    @Binds
    fun bindStreamEventHandler(impl: StreamEventHandlerImpl): StreamEventHandler
}