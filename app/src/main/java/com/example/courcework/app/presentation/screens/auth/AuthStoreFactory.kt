package com.example.courcework.app.presentation.screens.auth

import com.example.courcework.domain.repository.UserRepository
import com.github.terrakok.cicerone.Router
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class AuthStoreFactory @Inject constructor(
    private val router: Router,
    private val userRepository: UserRepository
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = AuthState(),
        reducer = AuthReducer,
        actor = AuthActor(
            userRepository,
            router
        )
    ) as ElmStoreCompat<AuthEvent, AuthState, AuthEffect, AuthCommand>
}