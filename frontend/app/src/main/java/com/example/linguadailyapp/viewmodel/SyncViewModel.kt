package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.datamodels.AvailableWord
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.utils.WordSyncLogic
import com.example.linguadailyapp.utils.preferences.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncViewModel(private val wordSyncLogic: WordSyncLogic) : ViewModel() {

    fun syncInBackground(preferencesManager: PreferencesManager) {
        viewModelScope.launch(Dispatchers.Main) {
            val languages = wordSyncLogic.shouldSyncForLanguages()

            wordSyncLogic.syncBlockingForLanguages(preferencesManager, languages)
        }
    }

    suspend fun syncBlocking(preferencesManager: PreferencesManager) {
        val languages = wordSyncLogic.shouldSyncForLanguages()

        wordSyncLogic.syncBlockingForLanguages(preferencesManager, languages)

    }

    suspend fun shouldSync() : Boolean {
        return wordSyncLogic.shouldSync()
    }

}