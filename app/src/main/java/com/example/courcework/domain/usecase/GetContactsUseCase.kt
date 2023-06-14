package com.example.courcework.domain.usecase

import com.example.courcework.domain.model.Contact
import com.example.courcework.domain.repository.PeopleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContactsUseCase(private val peopleRepository: PeopleRepository) {
    operator fun invoke(): Flow<List<Contact>> = peopleRepository.getContacts().map { contacts ->
        contacts.sortedWith(compareBy({ it.presence }, { it.name }))
    }
}