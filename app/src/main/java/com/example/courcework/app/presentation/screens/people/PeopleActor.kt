package com.example.courcework.app.presentation.screens.people

import com.example.courcework.app.presentation.catchNonCancellationAndEmit
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.navigation.Screens
import com.example.courcework.domain.usecase.GetContactSearchResultsUseCase
import com.example.courcework.domain.usecase.GetContactsUseCase
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor

class PeopleActor(
    private val router: Router,
    private val getContactsUseCase: GetContactsUseCase,
    private val getContactSearchResultsUseCase: GetContactSearchResultsUseCase,
    private val connectivityObserver: ConnectivityObserver,
) : Actor<PeopleCommand, PeopleEvent> {

    private val searchQueryState = MutableSharedFlow<String>()

    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            PeopleCommand.LoadContacts -> loadContacts()
            PeopleCommand.ObserveConnection -> observeConnection()
            PeopleCommand.SubscribeToSearchQueryState -> subscribeToSearchQueryState()
            is PeopleCommand.OpenProfile -> openProfile(command.contactId)
            is PeopleCommand.PerformSearch -> performSearch(command.text)
        }
    }

    private fun subscribeToSearchQueryState(): Flow<PeopleEvent> = searchQueryState
        .debounce(500L)
        .distinctUntilChanged()
        .filter { it.isEmpty() || it.isNotBlank() }
        .flatMapLatest { search(it) }

    private fun performSearch(text: String): Flow<PeopleEvent> = flow {
        searchQueryState.emit(text)
    }

    private fun loadContacts(): Flow<PeopleEvent> =
        getContactsUseCase().map { contacts ->
            PeopleEvent.Internal.ContactsLoaded(
                contacts.map { it.toUI() }
            )
        }.catchNonCancellationAndEmit(
            PeopleEvent.Internal.ErrorLoading("Failed to load contacts")
        )

    private fun search(query: String): Flow<PeopleEvent> =
        getContactSearchResultsUseCase(query).map { contacts ->
            PeopleEvent.Internal.ContactsLoaded(
                contacts.map { it.toUI() }
            )
        }.catchNonCancellationAndEmit(
            PeopleEvent.Internal.ErrorLoading("Failed to load contacts")
        )

    private fun openProfile(contactId: Int): Flow<PeopleEvent> {
        router.navigateTo(Screens.Profile(contactId))
        return emptyFlow()
    }

    private fun observeConnection(): Flow<PeopleEvent> = connectivityObserver.observe().map {
        when (it) {
            ConnectivityObserver.Status.Available -> PeopleEvent.Internal.ConnectionStatusChanged(true)
            ConnectivityObserver.Status.Losing -> PeopleEvent.Internal.ConnectionStatusChanged(true)
            ConnectivityObserver.Status.Lost -> PeopleEvent.Internal.ConnectionStatusChanged(false)
            ConnectivityObserver.Status.Unavailable -> PeopleEvent.Internal.ConnectionStatusChanged(false)
        }
    }.distinctUntilChanged()
}