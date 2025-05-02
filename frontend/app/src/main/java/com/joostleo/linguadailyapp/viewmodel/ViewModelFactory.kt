package com.joostleo.linguadailyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.LanguagePreferencesManager
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager

class WordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            val learnedWordRepository = LearnedWordRepository(context)
            val availableWordRepository = AvailableWordRepository(context)
            val languageViewModel = LanguageViewModel(LanguagePreferencesManager(context))
            val wordSyncLogic = WordSyncLogic(availableWordRepository)
            val cooldownManager = RandomWordCooldownManager(context)
            return WordViewModel(learnedWordRepository, availableWordRepository, languageViewModel, wordSyncLogic, cooldownManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SyncViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            val availableWordRepository = AvailableWordRepository(context)
            val wordSyncLogic = WordSyncLogic(availableWordRepository)
            return SyncViewModel(wordSyncLogic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
