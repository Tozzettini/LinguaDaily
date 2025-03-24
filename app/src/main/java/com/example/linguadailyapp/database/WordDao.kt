package com.example.linguadailyapp.database

import androidx.room.*

@Dao
interface WordDao {
    @Insert
    suspend fun insert(word: Word)

    @Delete
    suspend fun delete(word: Word)

    @Query("SELECT * FROM words ORDER BY id DESC")
    suspend fun getAllWords(): List<Word>
}
