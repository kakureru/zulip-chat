package com.example.courcework.app.presentation.screens.chat.dialog.topicpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.toDomain
import com.example.courcework.app.presentation.screens.channels.stream.model.toUI
import com.example.courcework.domain.usecase.GetTopicsToMoveMessageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicPickerViewModel @Inject constructor(
    private val getTopicsToMoveMessageUseCase: GetTopicsToMoveMessageUseCase
) : ViewModel() {

    private val _dialogState = MutableStateFlow(TopicPickerState())
    val dialogState: StateFlow<TopicPickerState> = _dialogState.asStateFlow()

    private val _dialogEffect = MutableSharedFlow<TopicPickerEffect>()
    val dialogEffect: SharedFlow<TopicPickerEffect> = _dialogEffect.asSharedFlow()

    fun accept(event: TopicPickerEvent) {
        when (event) {
            is TopicPickerEvent.LoadTopics -> loadTopics(event.currentTopicId)
        }
    }

    private fun loadTopics(currentTopicId: TopicId) = viewModelScope.launch {
        runCatchingNonCancellation {
            _dialogState.update { it.copy(isLoading = true) }
            getTopicsToMoveMessageUseCase(currentTopicId = currentTopicId.toDomain()).collect { topics ->
                _dialogState.update { TopicPickerState(data = topics.map { it.toUI() }) }
            }
        }.onFailure {
            _dialogEffect.emit(baseError)
        }
    }

    companion object {
        private val baseError = TopicPickerEffect.Error("Failed to load topics")
    }
}