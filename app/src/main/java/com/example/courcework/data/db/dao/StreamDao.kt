package com.example.courcework.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.courcework.data.db.model.StreamEntity
import com.example.courcework.data.db.model.SubscribedStreamEntity
import com.example.courcework.data.db.model.TopicEntity

@Dao
interface StreamDao {

    @Query("SELECT * FROM streams")
    suspend fun getAllStreams(): List<StreamEntity>

    @Query("SELECT * FROM subscribed_streams")
    suspend fun getSubscribedStreams(): List<SubscribedStreamEntity>

    @Query("SELECT * FROM topics WHERE stream_id = :streamId")
    suspend fun getTopics(streamId: Int): List<TopicEntity>

    @Transaction
    suspend fun refreshSubscribedStreams(streams: List<SubscribedStreamEntity>) {
        clearSubscribedStreams()
        saveSubscribedStreams(streams)
    }

    @Query("DELETE FROM subscribed_streams")
    suspend fun clearSubscribedStreams()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubscribedStreams(streams: List<SubscribedStreamEntity>)

    @Transaction
    suspend fun refreshAllStreams(streams: List<StreamEntity>) {
        clearAllStreams()
        saveAllStreams(streams)
    }

    @Query("DELETE FROM streams")
    suspend fun clearAllStreams()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllStreams(streams: List<StreamEntity>)

    @Transaction
    suspend fun refreshTopics(topics: List<TopicEntity>, streamId: Int) {
        clearTopics(streamId)
        saveTopics(topics)
    }

    @Query("DELETE FROM topics WHERE stream_id = :streamId")
    suspend fun clearTopics(streamId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopics(topics: List<TopicEntity>)
}