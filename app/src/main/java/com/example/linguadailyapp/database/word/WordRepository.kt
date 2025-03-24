package com.example.linguadailyapp.database.word

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import java.time.LocalDate

class WordRepository(val context : Context) {

    val wordDao = LocalDatabase.getInstance(context).wordDao()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun wipeWordsTable() {
        wordDao.wipeWordsTable()
    }

    suspend fun getTodaysWord(): Word? {
        return wordDao.getWordForDay(LocalDate.now())
    }

    suspend fun getAllWords(): List<Word> {
        return wordDao.getAllWords().filter { word ->
            word.date >= LocalDate.now()
        }
    }

}

