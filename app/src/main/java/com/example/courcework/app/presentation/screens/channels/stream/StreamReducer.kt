package com.example.courcework.app.presentation.screens.channels.stream

import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val StreamReducer = StateReducer { event: StreamEvent, state: StreamState ->
    when (event) {
        is StreamEvent.UI.Initialize -> {
            Result(
                state = StreamState(isInitLoading = true),
                commands = listOf(
                    StreamCommand.LoadStreams,
                    StreamCommand.SubscribeToSearchQueryState,
                )
            )
        }
        is StreamEvent.UI.SearchTextChanged -> {
            Result(
                state = state.copy(isLoading = true, data = state.data),
                command = StreamCommand.PerformSearch(event.text)
            )
        }
        is StreamEvent.UI.StreamClick -> {
            if (state.data != null) {
                Result(
                    state =  state.copy(isLoading = true, data = state.data),
                    command = StreamCommand.OnStreamClick(event.streamId, state.data)
                )
            } else {
                Result(state = state)
            }
        }
        is StreamEvent.UI.TopicClick -> {
            Result(
                state = state,
                command = StreamCommand.OpenChat(event.topicId)
            )
        }
        is StreamEvent.Internal.StreamsLoaded -> {
            Result(
                state = state.copy(data = event.data, isLoading = false, isInitLoading = false)
            )
        }
        is StreamEvent.Internal.TopicsLoaded -> {
            Result(
                state = state.copy(data = event.data, isLoading = false)
            )
        }
        is StreamEvent.Internal.ErrorLoading -> {
            Result(
                state = state,
                effect = StreamEffect.Error(event.msg)
            )
        }
    }
}