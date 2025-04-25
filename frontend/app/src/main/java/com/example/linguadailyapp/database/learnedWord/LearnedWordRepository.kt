package com.example.linguadailyapp.database.learnedWord

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.datamodels.LearnedWord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LearnedWordRepository(val context : Context) {

    val wordDao = LocalDatabase.getInstance(context).learnedWordDao()

    suspend fun insert(learnedWord: LearnedWord) {
        wordDao.insert(learnedWord)
    }

    suspend fun insertAll(learnedWords: List<LearnedWord>) {
        for(word in learnedWords) {
            this.insert(word)
        }
    }

    suspend fun updateWord(learnedWord: LearnedWord) {
        wordDao.updateWord(learnedWord)
    }

    suspend fun getTodaysWordForLanguage(language: Language): LearnedWord? {
        return wordDao.getWordsForDay(LocalDate.now()).firstOrNull { word ->
           word.isWordOfTheDay && word.language == language
        }
    }

    fun getAllWordsFlow(): Flow<List<LearnedWord>> {
        return wordDao.getAllWordsFlow()
    }

    suspend fun getWordById(id: Int): LearnedWord? {
        return wordDao.getWordById(id)
    }


}

