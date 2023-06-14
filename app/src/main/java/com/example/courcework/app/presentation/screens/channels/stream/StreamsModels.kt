package com.example.courcework.app.presentation.screens.channels.stream

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId

data class StreamState (
    val isInitLoading: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<DelegateItem>? = null
)

sealed class StreamEvent {

    sealed class UI : StreamEvent() {
        object Initialize : StreamEvent()
        class StreamClick(val streamId: Int) : StreamEvent()
        class TopicClick(val topicId: TopicId) : StreamEvent()
        class SearchTextChanged(val text: String) : StreamEvent()
    }

    sealed class Internal : StreamEvent() {
        class StreamsLoaded(val data: List<DelegateItem>) : StreamEvent()
        class TopicsLoaded(val data: List<DelegateItem>) : StreamEvent()
        class ErrorLoading(val msg: String) : StreamEvent()
    }
}

sealed class StreamEffect {
    class Error(val msg: String): StreamEffect()
}

sealed class StreamCommand {
    object LoadStreams : StreamCommand()
    object SubscribeToSearchQueryState : StreamCommand()
    class PerformSearch(val text: String) : StreamCommand()
    class OnStreamClick(val streamId: Int, val currentData: List<DelegateItem>) : StreamCommand()
    class OpenChat(val topicId: TopicId) : StreamCommand()
}