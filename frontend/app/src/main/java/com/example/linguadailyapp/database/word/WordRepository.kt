package com.example.linguadailyapp.database.word

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class WordRepository(val context : Context) {

    val wordDao = LocalDatabase.getInstance(context).wordDao()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun insertAll(words: List<Word>) {
        for(word in words) {
            this.insert(word)
        }
    }

    suspend fun getWordCount() : Int {
        return wordDao.getWordCount()
    }

    suspend fun updateWord(word: Word) {
        wordDao.updateWord(word)
    }

    suspend fun getTodaysWord(): Word? {
        return wordDao.getWordForDay(LocalDate.now())
    }

    fun getAllWordsFlow(): Flow<List<Word>> {
        return wordDao.getAllWordsFlow()
            .map { words -> words.filter { it.date <= LocalDate.now() } }
    }


}

