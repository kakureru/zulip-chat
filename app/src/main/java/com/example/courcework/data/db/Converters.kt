package com.example.courcework.data.db

import androidx.room.TypeConverter
import com.example.courcework.domain.model.Presence
import com.example.courcework.domain.model.TopicId
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun dateTimeToTimestamp(value: LocalDateTime?): Long? =
        value?.let {
            ZonedDateTime.of(value, ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

    @TypeConverter
    fun dateTimeFromTimestamp(value: Long?): LocalDateTime? =
        value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }

    @TypeConverter
    fun presenceToString(value: Presence): String =
        when (value) {
            Presence.ACTIVE -> "ACTIVE"
            Presence.IDLE -> "IDLE"
            Presence.OFFLINE -> "OFFLINE"
        }

    @TypeConverter
    fun stringToPresence(value: String): Presence =
        when (value) {
            "ACTIVE" -> Presence.ACTIVE
            "IDLE" -> Presence.IDLE
            else -> Presence.OFFLINE
        }

    @TypeConverter
    fun topicIdToString(value: TopicId): String = Gson().toJson(value)

    @TypeConverter
    fun stringToTopicId(value: String): TopicId = Gson().fromJson(value, TopicId::class.java)
}