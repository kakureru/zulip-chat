package com.example.courcework.app.presentation.screens.people

import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.domain.usecase.GetContactSearchResultsUseCase
import com.example.courcework.domain.usecase.GetContactsUseCase
import com.github.terrakok.cicerone.Router
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class PeopleStoreFactory @Inject constructor(
    private val router: Router,
    private val getContactsUseCase: GetContactsUseCase,
    private val getContactSearchResultsUseCase: GetContactSearchResultsUseCase,
    private val connectivityObserver: ConnectivityObserver,
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = PeopleState(),
        reducer = PeopleReducer,
        actor = PeopleActor(router, getContactsUseCase, getContactSearchResultsUseCase, connectivityObserver)
    ) as ElmStoreCompat<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>
}