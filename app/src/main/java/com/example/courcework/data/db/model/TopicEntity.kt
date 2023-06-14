package com.example.courcework.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.courcework.data.db.Converters
import com.example.courcework.domain.model.TopicId

@Entity(tableName = "topics")
@TypeConverters(Converters::class)
data class TopicEntity(
    @PrimaryKey val id: TopicId,
    @ColumnInfo(name = "stream_id") val streamId: Int,
    @ColumnInfo(name = "name") val name: String,
)