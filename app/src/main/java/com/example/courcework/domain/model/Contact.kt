package com.example.courcework.domain.model

import com.example.courcework.data.db.model.ContactEntity
import com.example.courcework.data.network.model.users.UserDto

data class Contact (
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val presence: Presence
)

fun UserDto.toDomain(presence: Presence): Contact = Contact(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    presence = presence
)

fun ContactEntity.toDomain() = Contact(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    presence = presence
)