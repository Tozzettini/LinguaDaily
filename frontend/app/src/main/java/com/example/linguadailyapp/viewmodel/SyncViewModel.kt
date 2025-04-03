package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.linguadailyapp.database.settings.SettingsRepository
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import com.example.linguadailyapp.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime

class SyncViewModel(private val wordRepository: WordRepository, private val settingsRepository: SettingsRepository) : ViewModel() {

    private suspend fun syncData(lastSynced: LocalDateTime): Boolean {
        return try {
            val words = RetrofitClient.apiService.getWordsSince(lastSynced.toString())
            wordRepository.insertAll(words)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun sync(preferencesManager: PreferencesManager) {
        val lastSynced = preferencesManager.getLastSynced()

        GlobalScope.launch(Dispatchers.Main) {
            val success = withContext(Dispatchers.IO) {
                syncData(lastSynced)
            }

            if (success) {
                preferencesManager.setLastSynced(LocalDateTime.now())
            }
        }
    }
}