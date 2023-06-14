package com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.domain.RequestStatus
import com.example.courcework.domain.model.NewStreamParams
import com.example.courcework.domain.repository.StreamRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewStreamViewModel @Inject constructor(
    private val streamRepository: StreamRepository
) : ViewModel() {

    private val _dialogState = MutableStateFlow(NewStreamState())
    val dialogState: StateFlow<NewStreamState> = _dialogState.asStateFlow()

    private val _dialogEffect = MutableSharedFlow<NewStreamEffect>()
    val dialogEffect: SharedFlow<NewStreamEffect> = _dialogEffect.asSharedFlow()

    fun accept(event: NewStreamEvent) {
        when (event) {
            is NewStreamEvent.Confirm -> createStream(event.params)
        }
    }

    private fun createStream(params: NewStreamParams) = viewModelScope.launch {
        if (params.name.isEmpty())
            _dialogEffect.emit(NewStreamEffect.Error("A stream needs to have a name"))
        else
            runCatchingNonCancellation {
                _dialogState.update { it.copy(isLoading = true) }
                when(streamRepository.createNewStream(params)) {
                    RequestStatus.Success -> {
                        _dialogState.update { NewStreamState(isSuccess = true) }
                    }
                    RequestStatus.Failure -> {
                        _dialogState.update { it.copy(isLoading = false) }
                        _dialogEffect.emit(baseError)
                    }
                }
            }.onFailure {
                _dialogEffect.emit(baseError)
            }
    }

    companion object {
        private val baseError = NewStreamEffect.Error("Failed to create stream")
    }
}