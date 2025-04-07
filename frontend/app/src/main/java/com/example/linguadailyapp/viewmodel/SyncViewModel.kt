package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.retrofit.RetrofitClient
import com.example.linguadailyapp.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class SyncViewModel(private val wordRepository: WordRepository) : ViewModel() {

    private suspend fun syncData(lastSynced: LocalDateTime): Boolean {
        return try {
            val words = if(!lastSynced.isEqual(LocalDateTime.MIN)) RetrofitClient.apiService.getWordsSince(lastSynced.toString()) else RetrofitClient.apiService.getAllWords()
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