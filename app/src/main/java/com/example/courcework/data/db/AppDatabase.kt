package com.example.courcework.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.db.dao.UserDao
import com.example.courcework.data.db.model.*

@Database(
    entities = [
        ContactEntity::class,
        MessageEntity::class,
        ReactionEntity::class,
        StreamEntity::class,
        SubscribedStreamEntity::class,
        TopicEntity::class,
        UserEntity::class,
    ],
    version = 11
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun peopleDao(): PeopleDao
    abstract fun userDao(): UserDao
    abstract fun streamDao(): StreamDao
}