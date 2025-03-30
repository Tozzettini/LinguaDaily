package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.linguadailyapp.database.settings.SettingsRepository
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import java.time.Instant

class SyncViewModel(private val wordRepository: WordRepository, private val settingsRepository: SettingsRepository) : ViewModel() {

    suspend fun sync() {
        val lastSynced = settingsRepository.getLastSynced()

        val words = RetrofitClient.apiService.getWordsSince(lastSynced.toString())

        wordRepository.insertAll(words)

        settingsRepository.setLastSynced(Instant.now())

    }
}