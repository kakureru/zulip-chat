package com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog

import com.example.courcework.domain.model.NewStreamParams

data class NewStreamState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

sealed class NewStreamEvent {
    class Confirm(val params: NewStreamParams) : NewStreamEvent()
}

sealed class NewStreamEffect {
    class Error(val msg: String) : NewStreamEffect()
}