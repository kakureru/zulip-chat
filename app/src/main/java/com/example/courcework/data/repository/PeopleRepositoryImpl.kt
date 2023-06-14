package com.example.courcework.data.repository

import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.db.model.ContactEntity
import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.model.users.UserDto
import com.example.courcework.domain.model.Contact
import com.example.courcework.domain.model.Presence
import com.example.courcework.domain.model.toDomain
import com.example.courcework.domain.repository.PeopleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val peopleDao: PeopleDao,
    private val userApi: UserApi
) : PeopleRepository {

    override fun getContacts(): Flow<List<Contact>> =
        getCacheContacts().also { loadFreshContacts() }

    private fun getCacheContacts(): Flow<List<Contact>> =
        peopleDao.getContacts().map { contacts -> contacts.map { it.toDomain() } }.distinctUntilChanged()

    private fun loadFreshContacts() = CoroutineScope(Dispatchers.IO).launch {
        val presence: Map<String, Presence> = getAllPresence()
        val freshContacts = userApi.getAllUsers().users.filter { it.isActive && it.isBot.not() }.map {
            it.toEntity(presence = presence.getOrDefault(it.email, Presence.OFFLINE))
        }
        peopleDao.refreshContacts(freshContacts)
    }

    private suspend fun getAllPresence(): Map<String, Presence> =
        userApi.getAllPresence().let {
            it.presences.mapValues { entry ->
                entry.value.presence.toDomain(it.serverTimestamp)
            }
        }

    override suspend fun getContact(contactId: Int): Contact =
        getUser(contactId).toDomain(getPresence(contactId))

    private suspend fun getUser(userId: Int): UserDto =
        userApi.getUser(userId).user

    private suspend fun getPresence(userId: Int): Presence =
        userApi.getUserPresence(userId).presence.presence.toDomain()

    private fun UserDto.toEntity(presence: Presence) = ContactEntity(
        id = id,
        name = name,
        email = email,
        avatarUrl = avatarUrl,
        presence = presence
    )
}