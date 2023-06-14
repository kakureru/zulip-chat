package com.example.courcework.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class MessageWithReactions(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id"
    )
    val reactions: List<ReactionEntity>
)