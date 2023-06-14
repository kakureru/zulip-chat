package com.example.courcework.app.presentation.screens.channels.stream

import com.example.courcework.domain.usecase.GetStreamSearchResultUseCase
import com.example.courcework.domain.usecase.GetStreamsUseCase
import com.example.courcework.domain.usecase.GetTopicsUseCase
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.coroutines.ElmStoreCompat

class StreamStoreFactory @AssistedInject constructor(
    @Assisted("showSubscribed") private val showSubscribed: Boolean,
    private val router: Router,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getStreamSearchResultUseCase: GetStreamSearchResultUseCase
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = StreamState(),
        reducer = StreamReducer,
        actor = StreamActor(showSubscribed, router, getTopicsUseCase, getStreamsUseCase, getStreamSearchResultUseCase)
    ) as ElmStoreCompat<StreamEvent, StreamState, StreamEffect, StreamCommand>

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("showSubscribed") showSubscribed: Boolean,
        ) : StreamStoreFactory
    }
}