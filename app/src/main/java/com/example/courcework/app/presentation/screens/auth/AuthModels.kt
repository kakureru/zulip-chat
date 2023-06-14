package com.example.courcework.app.presentation.screens.auth

import com.example.courcework.data.network.model.users.AuthCredentials

data class AuthState(
    val isLoading: Boolean = false
)

sealed class AuthEvent {
    sealed class UI {
        object OnOpen: AuthEvent()
        class SingInClick(val authCredentials: AuthCredentials): AuthEvent()
    }

    sealed class Internal {
        class ErrorSingIn(val msg: String): AuthEvent()
    }
}

sealed class AuthEffect {
    class Error(val msg: String): AuthEffect()
}

sealed class AuthCommand {
    class SignIn(val authCredentials: AuthCredentials): AuthCommand()
}