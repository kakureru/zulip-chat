package com.example.courcework.domain.model

import com.example.courcework.data.db.model.MessageWithReactions
import com.example.courcework.data.network.model.messages.MessageDto
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
data class Message(
    val id: Int,
    val senderId: Int,
    val author: String,
    val text: String,
    val avatarUrl: String?,
    val dateTime: LocalDateTime,
    val reactions: List<Reaction>,
    val streamId: Int
)

fun MessageDto.toDomain(): Message = Message(
    id = id,
    senderId = senderId,
    author = name,
    text = content,
    avatarUrl = avatarUrl,
    dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId()),
    reactions = reactions?.map { it.toDomain() } ?: listOf(),
    streamId = streamId
)

fun MessageWithReactions.toDomain(): Message = Message(
    id = message.id,
    senderId = message.senderId,
    author = message.author,
    text = message.text,
    avatarUrl = message.avatarUrl,
    dateTime = message.dateTime,
    reactions = reactions.map { it.toDomain() },
    streamId = message.topicId.streamId
)