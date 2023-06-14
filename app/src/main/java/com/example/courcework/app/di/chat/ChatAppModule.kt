package com.example.courcework.app.di.chat

import androidx.lifecycle.ViewModel
import com.example.courcework.app.di.ViewModelKey
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChatAppModule {

    @ChatScope
    @Binds
    @IntoMap
    @ViewModelKey(TopicPickerViewModel::class)
    fun bindsTopicPickerViewModel(viewModel: TopicPickerViewModel): ViewModel
}