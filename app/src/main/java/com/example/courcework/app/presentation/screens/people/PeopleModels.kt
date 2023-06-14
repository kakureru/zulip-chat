package com.example.courcework.app.presentation.screens.people

data class PeopleState(
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isConnectionAvailable: Boolean = false,
    val error: String? = null,
    val data: List<ContactItem>? = null
)

sealed class PeopleEvent {

    sealed class UI : PeopleEvent() {
        object Initialize : PeopleEvent()
        class ContactClick(val contactId: Int) : PeopleEvent()
        class SearchTextChanged(val text: String) : PeopleEvent()
    }

    sealed class Internal : PeopleEvent() {
        class ContactsLoaded(val data: List<ContactItem>) : PeopleEvent()
        class ErrorLoading(val msg: String) : PeopleEvent()
        class ConnectionStatusChanged(val isAvailable: Boolean) : PeopleEvent()
    }
}

sealed class PeopleEffect {
    class Error(val msg: String): PeopleEffect()
}

sealed class PeopleCommand {
    object LoadContacts : PeopleCommand()
    object ObserveConnection : PeopleCommand()
    object SubscribeToSearchQueryState : PeopleCommand()
    class OpenProfile(val contactId: Int) : PeopleCommand()
    class PerformSearch(val text: String) : PeopleCommand()
}