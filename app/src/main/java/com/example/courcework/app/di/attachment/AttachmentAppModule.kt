package com.example.courcework.app.di.attachment

import androidx.lifecycle.ViewModel
import com.example.courcework.app.di.ViewModelKey
import com.example.courcework.app.presentation.screens.chat.attachment.AttachmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AttachmentAppModule {

    @AttachmentScope
    @Binds
    @IntoMap
    @ViewModelKey(AttachmentViewModel::class)
    fun bindsAttachmentViewModel(viewModel: AttachmentViewModel): ViewModel
}