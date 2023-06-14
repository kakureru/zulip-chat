package com.example.courcework.app.presentation.screens.chat.dialog.topicpicker

import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicItem

data class TopicPickerState(
    val isLoading: Boolean = false,
    val data: List<TopicItem> = emptyList()
)

sealed class TopicPickerEvent {
    class LoadTopics(val currentTopicId: TopicId) : TopicPickerEvent()
}

sealed class TopicPickerEffect {
    class Error(val msg: String) : TopicPickerEffect()
}