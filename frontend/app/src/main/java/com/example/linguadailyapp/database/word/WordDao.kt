package com.example.linguadailyapp.database.word

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WordDao {
    @Insert
    suspend fun insert(word: Word)

    @Delete
    suspend fun delete(word: Word)

    @Query("DELETE FROM words")
    suspend fun wipeWordsTable()

    @Query("SELECT * FROM words WHERE date = :day LIMIT 1")
    suspend fun getWordForDay(day: LocalDate): Word?

    @Query("SELECT * FROM words ORDER BY date DESC")
    suspend fun getAllWords(): List<Word>

    @Query("SELECT * FROM words ORDER BY date DESC")
    fun getAllWordsFlow(): Flow<List<Word>>
}
