package com.example.courcework.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.courcework.data.db.Converters
import com.example.courcework.domain.model.TopicId
import java.time.LocalDateTime

@Entity(tableName = "messages")
@TypeConverters(Converters::class)
data class MessageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "topic_id") val topicId: TopicId,
    @ColumnInfo(name = "sender_id") val senderId: Int,
    @ColumnInfo(name = "author")val author: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "date_time") val dateTime: LocalDateTime
)