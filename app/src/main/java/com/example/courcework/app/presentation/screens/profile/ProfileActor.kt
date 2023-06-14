package com.example.courcework.app.presentation.screens.profile

import com.example.courcework.app.presentation.navigation.Screens
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.repository.PeopleRepository
import com.example.courcework.domain.repository.UserRepository
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

class ProfileActor(
    private val isOwnUser: Boolean,
    private val userId: Int?,
    private val authHelper: AuthHelper,
    private val router: Router,
    private val userRepository: UserRepository,
    private val peopleRepository: PeopleRepository
) : Actor<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Flow<ProfileEvent> =
        when (command) {
            ProfileCommand.LoadProfile -> getUser()
            ProfileCommand.Logout -> logout()
            ProfileCommand.OnBackPressed -> onBackPressed()
        }

    private fun getUser(): Flow<ProfileEvent> = flow {
        runCatchingNonCancellation {
            if (isOwnUser)
                emit(ProfileEvent.Internal.ProfileLoaded(userRepository.getUser().toUI()))
            else userId?.let {
                emit(ProfileEvent.Internal.ProfileLoaded(peopleRepository.getContact(userId).toProfileItem()))
            }
        }.onFailure {
            emit(ProfileEvent.Internal.ErrorLoading("Failed to load user"))
        }
    }

    private fun logout(): Flow<ProfileEvent> = flow {
        authHelper.apply {
            setAuthStatus(false)
            cleanAuthData()
        }
        router.replaceScreen(Screens.Auth())
    }

    private fun onBackPressed(): Flow<ProfileEvent> {
        router.exit()
        return emptyFlow()
    }
}