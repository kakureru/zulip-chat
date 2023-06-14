package com.example.courcework.data.db.dao

import androidx.room.*
import com.example.courcework.data.db.Converters
import com.example.courcework.data.db.model.MessageEntity
import com.example.courcework.data.db.model.MessageWithReactions
import com.example.courcework.data.db.model.ReactionEntity
import com.example.courcework.domain.model.TopicId

@Dao
interface MessageDao {

    @TypeConverters(Converters::class)
    @Transaction
    @Query("SELECT * FROM messages WHERE topic_id = :topicId")
    suspend fun getTopicMessages(topicId: TopicId): List<MessageWithReactions>

    @Transaction
    suspend fun refreshMessages(messages: List<MessageEntity>, reactions: List<ReactionEntity>) {
        clearMessages()
        clearReactions()
        saveMessages(messages)
        saveReactions(reactions)
    }

    @Query("DELETE FROM messages")
    suspend fun clearMessages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM reactions")
    suspend fun clearReactions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReactions(reactions: List<ReactionEntity>)
}