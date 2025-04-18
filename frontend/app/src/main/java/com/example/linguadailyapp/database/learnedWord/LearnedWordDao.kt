package com.example.linguadailyapp.database.learnedWord

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LearnedWordDao {
    @Insert
    suspend fun insert(learnedWord: LearnedWord)

    @Delete
    suspend fun delete(learnedWord: LearnedWord)

    @Query("DELETE FROM learnedWords")
    suspend fun wipeWordsTable()

    @Query("SELECT * FROM learnedWords WHERE learnedAt = :day")
    suspend fun getWordsForDay(day: LocalDate): List<LearnedWord>

    @Query("SELECT * FROM learnedWords ORDER BY learnedAt DESC")
    fun getAllWordsFlow(): Flow<List<LearnedWord>>

    @Update
    suspend fun updateWord(learnedWord: LearnedWord)

    @Query("SELECT * FROM learnedWords WHERE id = :id")
    suspend fun getWordById(id: Int) : LearnedWord?
}
