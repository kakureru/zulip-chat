package com.example.courcework.domain.model

import com.example.courcework.data.db.model.UserEntity
import com.example.courcework.data.network.model.users.OwnUserResponse

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val presence: Presence
)

fun OwnUserResponse.toDomain(presence: Presence): User = User(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    presence = presence
)

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    presence = presence
)