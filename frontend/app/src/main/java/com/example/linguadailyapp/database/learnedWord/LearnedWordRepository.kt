package com.example.linguadailyapp.database.learnedWord

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    suspend fun getTodaysWord(): LearnedWord? {
        return wordDao.getWordsForDay(LocalDate.now()).firstOrNull { word ->
           word.isWordOfTheDay
        }
    }

    fun getAllWordsFlow(): Flow<List<LearnedWord>> {
        return wordDao.getAllWordsFlow()
    }


}

