package com.joostleo.linguadailyapp.database.availableword

import android.content.Context
import com.joostleo.linguadailyapp.database.LocalDatabase
import com.joostleo.linguadailyapp.datamodels.AvailableWord
import com.joostleo.linguadailyapp.datamodels.Language

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

    suspend fun getWordCountForLanguage(language: Language) : Int {
        return wordDao.getWordCountForLanguage(language)
    }

    suspend fun removeWord(availableWord: AvailableWord) {
        wordDao.delete(availableWord)
    }

    suspend fun getRandomWordForLanguage(language: Language) : AvailableWord? {
        if(this.getWordCountForLanguage(language) == 0) return null

        return wordDao.getAllWords().filter { it -> it.language == language }.random()
    }

}

