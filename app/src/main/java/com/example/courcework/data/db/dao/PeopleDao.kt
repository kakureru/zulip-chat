package com.example.courcework.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.courcework.data.db.model.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PeopleDao {

    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<ContactEntity>>

    @Transaction
    suspend fun refreshContacts(contacts: List<ContactEntity>) {
        clearContacts()
        saveContacts(contacts)
    }

    @Query("DELETE FROM contacts")
    suspend fun clearContacts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContacts(contacts: List<ContactEntity>)
}