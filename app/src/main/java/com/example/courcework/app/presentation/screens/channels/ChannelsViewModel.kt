package com.example.courcework.app.presentation.screens.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(ChannelsState())
    val state: StateFlow<ChannelsState> = _state.asStateFlow()

    init {
        observeConnection()
    }

    private fun observeConnection() {
        connectivityObserver.observe().map { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> _state.value.copy(isConnectionAvailable = true)
                ConnectivityObserver.Status.Losing -> _state.value.copy(isConnectionAvailable = true)
                ConnectivityObserver.Status.Lost -> _state.value.copy(isConnectionAvailable = false)
                ConnectivityObserver.Status.Unavailable -> _state.value.copy(isConnectionAvailable = false)
            }
        }
            .distinctUntilChanged()
            .map { newState -> _state.update { newState } }
            .launchIn(viewModelScope)
    }
}