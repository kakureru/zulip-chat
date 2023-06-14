package com.example.courcework.app.presentation.screens.people

import com.example.courcework.domain.model.Contact
import com.example.courcework.domain.model.Presence

data class ContactItem (
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    var presence: Presence
)

fun Contact.toUI() = ContactItem(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    presence = presence
)