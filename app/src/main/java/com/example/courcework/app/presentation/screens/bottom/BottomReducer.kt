package com.example.courcework.app.presentation.screens.bottom

import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result

val BottomReducer = StateReducer { event: BottomEvent, state: BottomState ->
    when (event) {
        is BottomEvent.UI.BottomNavClick -> {
            Result(
                state = state,
                command = BottomCommand.SwitchTab(event.menuItem)
            )
        }
        is BottomEvent.Internal.SwitchedToChannels -> {
            Result(
                state = BottomState.Channels
            )
        }
        is BottomEvent.Internal.SwitchedToPeople -> {
            Result(
                state = BottomState.People
            )
        }
        is BottomEvent.Internal.SwitchedToProfile -> {
            Result(
                state = BottomState.Profile
            )
        }
        BottomEvent.UI.OpenDefaultTab -> {
            Result(
                state = state,
                command = BottomCommand.OpenDefaultTab
            )
        }
    }
}