package com.example.courcework.app.presentation.screens.auth

import com.example.courcework.app.presentation.navigation.Screens
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.data.network.model.users.AuthCredentials
import com.example.courcework.domain.repository.UserRepository
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

class AuthActor(
    private val userRepository: UserRepository,
    private val router: Router
) : Actor<AuthCommand.SignIn, AuthEvent> {

    override fun execute(command: AuthCommand.SignIn): Flow<AuthEvent> =
        signIn(command.authCredentials)

    private fun signIn(authCredentials: AuthCredentials): Flow<AuthEvent> = flow {
        runCatchingNonCancellation {
            if (authCredentials.check()) {
                userRepository.signIn(authCredentials)
                router.replaceScreen(Screens.BottomNavigation())
            } else
                emit(
                    AuthEvent.Internal.ErrorSingIn(
                        "Wrong credentials!"
                    )
                )
        }.onFailure {
            emit(
                AuthEvent.Internal.ErrorSingIn(
                    "Failed to sign in"
                )
            )
        }
    }

    private fun AuthCredentials.check(): Boolean = login.isNotBlank() && apiKey.isNotBlank()

    private fun signUp(): Flow<AuthEvent> = flow {

    }
}