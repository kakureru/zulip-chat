package com.example.courcework.app.presentation.screens.channels.stream

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.catchNonCancellationAndEmit
import com.example.courcework.app.presentation.navigation.Screens
import com.example.courcework.app.presentation.screens.channels.stream.adapter.StreamDelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.adapter.TopicDelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.StreamItem
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.toUI
import com.example.courcework.domain.model.Topic
import com.example.courcework.domain.usecase.GetStreamSearchResultUseCase
import com.example.courcework.domain.usecase.GetStreamsUseCase
import com.example.courcework.domain.usecase.GetTopicsUseCase
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor

class StreamActor(
    private val showSubscribed: Boolean,
    private val router: Router,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getStreamSearchResultUseCase: GetStreamSearchResultUseCase
) : Actor<StreamCommand, StreamEvent> {

    private val searchQueryState: MutableSharedFlow<String> = MutableSharedFlow()

    override fun execute(command: StreamCommand): Flow<StreamEvent> {
        return when (command) {
            StreamCommand.LoadStreams -> loadStreams()
            StreamCommand.SubscribeToSearchQueryState -> subscribeToSearchQueryState()
            is StreamCommand.PerformSearch -> performSearch(command.text)
            is StreamCommand.OnStreamClick -> onStreamClick(command.streamId, command.currentData)
            is StreamCommand.OpenChat -> openChat(command.topicId)
        }
    }

    private fun loadStreams(): Flow<StreamEvent> = getStreamsUseCase(showSubscribed).map { streams ->
        StreamEvent.Internal.StreamsLoaded(
            streams.map { it.toUI() }.map { it.toStreamDelegateItem() }
        )
    }.catchNonCancellationAndEmit(
        StreamEvent.Internal.ErrorLoading("Failed to load streams")
    )

    private fun subscribeToSearchQueryState(): Flow<StreamEvent> = searchQueryState
        .debounce(500L)
        .distinctUntilChanged()
        .filter { it.isEmpty() || it.isNotBlank() }
        .flatMapLatest { search(it) }

    private fun performSearch(text: String): Flow<StreamEvent> = flow {
        searchQueryState.emit(text)
    }

    private fun search(query: String): Flow<StreamEvent> =
        getStreamSearchResultUseCase(query, showSubscribed).map { streams ->
            StreamEvent.Internal.StreamsLoaded(
                streams.map { it.toUI() }.map { it.toStreamDelegateItem() }
            )
        }.catchNonCancellationAndEmit(
            StreamEvent.Internal.ErrorLoading("Failed to load streams")
        )

    private fun openChat(topicId: TopicId): Flow<StreamEvent> {
        router.navigateTo(Screens.Chat(topicId))
        return emptyFlow()
    }

    /**
     * Topic loading
     */

    private fun onStreamClick(streamId: Int, data: List<DelegateItem>): Flow<StreamEvent> {
        val streamItem = data.find { it is StreamDelegateItem && it.id() == streamId }?.content() as StreamItem
        return if (streamItem.expanded)
            data.removeTopics(streamId)
        else
            data.loadTopics(streamId)
    }

    private fun List<DelegateItem>.removeTopics(streamId: Int): Flow<StreamEvent> = flow {
        val delegateItemList = mutableListOf<DelegateItem>()

        val streamIndex = indexOfFirst { it is StreamDelegateItem && it.id() == streamId }
        delegateItemList.addAll(
            slice(0 until streamIndex)
                    + (get(streamIndex) as StreamDelegateItem).toCollapsedStreamDelegateItem()
        )
        subList(streamIndex + 1, size).indexOfFirst { it is StreamDelegateItem }.takeIf { it >= 0 }?.let {
            delegateItemList.addAll(slice(it + streamIndex + 1 until size))
        }

        emit(StreamEvent.Internal.TopicsLoaded(delegateItemList))
    }

    private fun List<DelegateItem>.loadTopics(streamId: Int): Flow<StreamEvent> = getTopicsUseCase(streamId).map {
        StreamEvent.Internal.TopicsLoaded(
            this@loadTopics.addTopics(streamId, it)
        )
    }.catchNonCancellationAndEmit(
        StreamEvent.Internal.ErrorLoading("Failed to load topics")
    )

    private fun List<DelegateItem>.addTopics(streamId: Int, topicList: List<Topic>): List<DelegateItem> {
        val delegateItemList = mutableListOf<DelegateItem>()

        this.forEach { delegateItem ->
            if (delegateItem is StreamDelegateItem && delegateItem.id() == streamId) {
                delegateItemList.add(delegateItem.toExpandedStreamDelegateItem())
                delegateItemList.addAll(topicList.map { TopicDelegateItem(it.toUI()) })
            } else
                delegateItemList.add(delegateItem)
        }

        return delegateItemList
    }

    private fun StreamItem.toStreamDelegateItem() = StreamDelegateItem(
        id = id,
        value = this
    )

    private fun StreamDelegateItem.toCollapsedStreamDelegateItem() = StreamDelegateItem(
        id = id,
        value = (this.content() as StreamItem).copy(expanded = false)
    )

    private fun StreamDelegateItem.toExpandedStreamDelegateItem() = StreamDelegateItem(
        id = id,
        value = (this.content() as StreamItem).copy(expanded = true)
    )
}