package com.example.courcework.domain.model

import com.example.courcework.data.db.model.ReactionEntity
import com.example.courcework.data.network.model.messages.ReactionDto

data class Reaction(
    val emoji: EmojiNCS,
    val userId: Int
)

fun ReactionDto.toDomain(): Reaction = Reaction(
    emoji = EmojiNCS(emojiName, emojiCode),
    userId = userId,
)

fun ReactionEntity.toDomain(): Reaction = Reaction(
    emoji = EmojiNCS(emojiName, emojiCode),
    userId = userId
)