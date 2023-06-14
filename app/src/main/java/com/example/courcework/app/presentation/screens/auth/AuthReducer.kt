package com.example.courcework.app.presentation.screens.auth

import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val AuthReducer = StateReducer { event: AuthEvent, state: AuthState ->
    when (event) {
        is AuthEvent.UI.SingInClick -> Result(
            state = AuthState(isLoading = true),
            command = AuthCommand.SignIn(event.authCredentials)
        )
        is AuthEvent.Internal.ErrorSingIn -> Result(
            state = AuthState(isLoading = false),
            effect = AuthEffect.Error(event.msg)
        )
        AuthEvent.UI.OnOpen -> Result(
            state = state
        )
    }
}