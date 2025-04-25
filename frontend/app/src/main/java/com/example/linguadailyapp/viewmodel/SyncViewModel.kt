package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.datamodels.AvailableWord
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.utils.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncViewModel(private val availableWordRepository: AvailableWordRepository) : ViewModel() {

    private val DATABASE_SIZE_LIMIT = 50

    suspend fun syncBlocking(preferencesManager: PreferencesManager) {
        for(language in Language.entries) {
            syncForLanguage(preferencesManager, language)
        }
    }

    fun syncInBackground(preferencesManager: PreferencesManager) {
        viewModelScope.launch(Dispatchers.Main) {
            syncBlocking(preferencesManager)
        }
    }

    private suspend fun syncForLanguage(preferencesManager: PreferencesManager, language: Language) {
        var successfullyAdded = mutableListOf<AvailableWord>()

        var countWords = availableWordRepository.getWordCountForLanguage(language = language)

        if(countWords == DATABASE_SIZE_LIMIT) return

        var skip = preferencesManager.getSkipForLanguage(language)
        var limit = DATABASE_SIZE_LIMIT - countWords


        try {
            var words = RetrofitClient.apiService.getWordsWithSkipAndLimit(skip, limit, language.name)

            for(word in words) {
                availableWordRepository.insert(word)
                successfullyAdded.add(word)
            }

            preferencesManager.setSkipForLanguage(skip + limit, language)
        } catch (e: Exception) {
            for(word in successfullyAdded) {
                availableWordRepository.removeWord(word)
            }
        }
    }

    suspend fun canSyncInBackground() : Boolean {
        for(language in Language.entries) {
            if(availableWordRepository.getWordCountForLanguage(language) == 0) return false
        }

        return true
    }

}