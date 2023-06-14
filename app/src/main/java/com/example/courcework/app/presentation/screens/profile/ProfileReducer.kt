package com.example.courcework.app.presentation.screens.profile

import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val ProfileReducer = StateReducer { event: ProfileEvent, state: ProfileState ->
    when (event) {
        is ProfileEvent.UI.LoadProfile -> {
            Result(
                state = ProfileState(isLoading = true),
                command = ProfileCommand.LoadProfile
            )
        }

        is ProfileEvent.Internal.ErrorLoading -> {
            Result(
                state = state,
                effect = ProfileEffect.Error(event.msg)
            )
        }

        is ProfileEvent.Internal.ProfileLoaded -> {
            Result(
                state = ProfileState(data = event.data)
            )
        }

        ProfileEvent.UI.LogoutClick -> Result(
            state = state,
            command = ProfileCommand.Logout
        )

        ProfileEvent.UI.BackPressed -> Result(
            state = state,
            command = ProfileCommand.OnBackPressed
        )
    }
}