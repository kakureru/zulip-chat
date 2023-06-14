package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContactSearchResultsUseCase(private val getContactsUseCase: GetContactsUseCase) {
    operator fun invoke(query: String): Flow<List<Contact>> = getContactsUseCase().map { contacts ->
        contacts.filter {
            it.name.lowercase().startsWith(query.lowercase())
        }
    }

}