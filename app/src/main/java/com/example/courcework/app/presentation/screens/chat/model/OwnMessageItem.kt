package com.example.courcework.app.presentation.screens.chat.model

import com.example.courcework.domain.model.Message
import java.time.format.DateTimeFormatter

data class OwnMessageItem(
    override val id: Int,
    override val text: String,
    override val date: String,
    override val time: String,
    override val reactions: List<ReactionItem>,
    val isSending: Boolean = false
) : MessageItem

fun Message.toOwnItem(ownUserId: Int) = OwnMessageItem(
    id = id,
    text = text,
    date = dateTime.format(DateTimeFormatter.ofPattern("dd LLLL")),
    time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
    reactions = reactions.groupBy { it.emoji.toUI() }.map {
        ReactionItem(
            emoji = it.key,
            count = it.value.size,
            selected = it.value.any { reaction -> reaction.userId == ownUserId }
        )
    }
)