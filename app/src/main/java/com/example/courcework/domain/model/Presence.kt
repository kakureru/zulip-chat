package com.example.courcework.domain.model

import com.example.courcework.data.network.model.users.presence.ClientPresenceDto
import java.util.concurrent.TimeUnit

enum class Presence {
    ACTIVE,
    IDLE,
    OFFLINE
}

fun ClientPresenceDto.toDomain(): Presence = when (status) {
    "idle" -> Presence.IDLE
    "active" -> Presence.ACTIVE
    else -> Presence.OFFLINE
}

fun ClientPresenceDto.toDomain(serverTimestamp: Float): Presence =
    if (serverTimestamp - timestamp > TimeUnit.MINUTES.toSeconds(10))
        Presence.OFFLINE
    else when (status) {
        "idle" -> Presence.IDLE
        "active" -> Presence.ACTIVE
        else -> throw IllegalArgumentException()
    }