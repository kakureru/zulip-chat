package com.example.courcework.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.courcework.data.db.Converters
import com.example.courcework.domain.model.Presence

@Entity(tableName = "contacts")
@TypeConverters(Converters::class)
data class ContactEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "presence") val presence: Presence
)