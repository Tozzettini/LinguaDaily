package com.joostleo.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.PreferencesManager
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