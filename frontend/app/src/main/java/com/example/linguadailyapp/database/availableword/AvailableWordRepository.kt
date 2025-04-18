package com.example.linguadailyapp.database.availableword

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import kotlin.random.Random
import kotlin.random.nextInt

class AvailableWordRepository(val context : Context) {

    val wordDao = LocalDatabase.getInstance(context).availableWordDao()

    suspend fun insert(availableWord: AvailableWord) {
        wordDao.insert(availableWord)
    }

    suspend fun insertAll(availableWords: List<AvailableWord>) {
        for(word in availableWords) {
            this.insert(word)
        }
    }

    suspend fun getWordCount() : Int {
        return wordDao.getWordCount()
    }

    suspend fun removeWord(availableWord: AvailableWord) {
        wordDao.delete(availableWord)
    }

    suspend fun getRandomWord() : AvailableWord? {
        if(this.getWordCount() == 0) return null

        var availableWords = wordDao.getAllWords()
        return availableWords.get(Random.nextInt(0 until  availableWords.size))
    }

}

