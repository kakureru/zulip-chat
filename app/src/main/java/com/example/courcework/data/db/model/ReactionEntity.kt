package com.example.courcework.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reactions")
class ReactionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "message_id") val messageId: Int,
    @ColumnInfo(name = "emoji_name") val emojiName: String,
    @ColumnInfo(name = "emoji_code") val emojiCode: String,
    @ColumnInfo(name = "user_id") val userId: Int,
)