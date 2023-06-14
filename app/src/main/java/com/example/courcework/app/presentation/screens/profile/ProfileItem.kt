package com.example.courcework.app.presentation.screens.profile

import com.example.courcework.domain.model.Contact
import com.example.courcework.domain.model.Presence
import com.example.courcework.domain.model.User

data class ProfileItem(
    val id: Int,
    val name: String,
    val presence: Presence,
    val avatarUrl: String?
)

fun User.toUI() = ProfileItem(
    id = id,
    name = name,
    presence = presence,
    avatarUrl = avatarUrl
)

fun Contact.toProfileItem() = ProfileItem(
    id = id,
    name = name,
    presence = presence,
    avatarUrl = avatarUrl
)