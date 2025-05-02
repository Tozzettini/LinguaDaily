package com.joostleo.linguadailyapp.database.availableword

import androidx.room.*
import com.joostleo.linguadailyapp.datamodels.AvailableWord
import com.joostleo.linguadailyapp.datamodels.Language

@Dao
interface AvailableWordDao {
    @Insert
    suspend fun insert(learnedWord: AvailableWord)

    @Delete
    suspend fun delete(learnedWord: AvailableWord)

    @Query("SELECT count(*) FROM availableWords WHERE language = :language")
    suspend fun getWordCountForLanguage(language: Language): Int

    @Update
    suspend fun updateWord(learnedWord: AvailableWord)

    @Query("SELECT * FROM availableWords")
    suspend fun getAllWords(): List<AvailableWord>
}
