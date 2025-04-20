package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.database.availableword.AvailableWord
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import com.example.linguadailyapp.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncViewModel(private val availableWordRepository: AvailableWordRepository) : ViewModel() {

    private val DATABASE_SIZE_LIMIT = 50

    suspend fun syncBlocking(preferencesManager: PreferencesManager) {
        var successfullyAdded = mutableListOf<AvailableWord>()

        var countWords = availableWordRepository.getWordCount()

        if(countWords == DATABASE_SIZE_LIMIT) return

        var skip = preferencesManager.getSkip()
        var limit = DATABASE_SIZE_LIMIT - countWords


        try {
            var words = RetrofitClient.apiService.getWordsWithSkipAndLimit(skip, limit)

            for(word in words) {
                availableWordRepository.insert(word)
                successfullyAdded.add(word)
            }

            preferencesManager.setSkip(skip + limit)
        } catch (e: Exception) {
            for(word in successfullyAdded) {
                availableWordRepository.removeWord(word)
            }
        }
    }

    fun syncInBackground(preferencesManager: PreferencesManager) {
        viewModelScope.launch(Dispatchers.Main) {
            syncBlocking(preferencesManager)
        }
    }

}