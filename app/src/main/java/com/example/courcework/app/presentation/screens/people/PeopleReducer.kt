package com.example.courcework.app.presentation.screens.people

import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val PeopleReducer = StateReducer { event: PeopleEvent, state: PeopleState ->
    when (event) {
        is PeopleEvent.UI.Initialize -> {
            Result(
                state = PeopleState(isLoading = true),
                commands = listOf(
                    PeopleCommand.LoadContacts,
                    PeopleCommand.ObserveConnection,
                    PeopleCommand.SubscribeToSearchQueryState,
                )
            )
        }
        is PeopleEvent.UI.ContactClick -> {
            Result(
                state = state,
                command = PeopleCommand.OpenProfile(event.contactId)
            )
        }
        is PeopleEvent.UI.SearchTextChanged -> {
            Result(
                state = state.copy(isLoading = false, isSearching = true, error = null),
                command = PeopleCommand.PerformSearch(event.text)
            )
        }
        is PeopleEvent.Internal.ContactsLoaded -> {
            Result(
                state = state.copy(data = event.data, isLoading = false, isSearching = false)
            )
        }
        is PeopleEvent.Internal.ErrorLoading -> {
            Result(
                state = state,
                effect = PeopleEffect.Error(event.msg)
            )
        }
        is PeopleEvent.Internal.ConnectionStatusChanged -> {
            Result(
                state = state.copy(isConnectionAvailable = event.isAvailable)
            )
        }
    }
}