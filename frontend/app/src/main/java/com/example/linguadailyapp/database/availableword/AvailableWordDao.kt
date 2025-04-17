package com.example.linguadailyapp.database.availableword

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AvailableWordDao {
    @Insert
    suspend fun insert(learnedWord: AvailableWord)

    @Delete
    suspend fun delete(learnedWord: AvailableWord)

    @Query("SELECT count(*) FROM availableWords")
    suspend fun getWordCount(): Int

    @Update
    suspend fun updateWord(learnedWord: AvailableWord)
}
