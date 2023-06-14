package com.example.courcework.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscribed_streams")
data class SubscribedStreamEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String
)