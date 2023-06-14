package com.example.courcework.app.presentation.screens.profile

import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.repository.PeopleRepository
import com.example.courcework.domain.repository.UserRepository
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ProfileStoreFactory @AssistedInject constructor(
    @Assisted("isOwnUser") private val isOwnUser: Boolean,
    @Assisted("userId") private val userId: Int?,
    private val authHelper: AuthHelper,
    private val router: Router,
    private val userRepository: UserRepository,
    private val peopleRepository: PeopleRepository
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = ProfileState(),
        reducer = ProfileReducer,
        actor = ProfileActor(
            isOwnUser = isOwnUser,
            userId = userId,
            authHelper = authHelper,
            router = router,
            userRepository = userRepository,
            peopleRepository = peopleRepository)
    ) as ElmStoreCompat<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("isOwnUser") isOwnUser: Boolean,
            @Assisted("userId")userId: Int?
        ) : ProfileStoreFactory
    }
}