package com.example.linguadailyapp.database.availableword

import androidx.room.*
import com.example.linguadailyapp.datamodels.AvailableWord
import com.example.linguadailyapp.datamodels.Language
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import java.time.LocalDate

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
