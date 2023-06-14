package com.example.courcework.domain.repository

import com.example.courcework.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {
    fun getContacts(): Flow<List<Contact>>
    suspend fun getContact(contactId: Int): Contact
}