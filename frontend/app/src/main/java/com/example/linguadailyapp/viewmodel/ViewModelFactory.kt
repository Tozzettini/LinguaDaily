package com.example.linguadailyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linguadailyapp.database.availableword.AvailableWord
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.database.learnedWord.LearnedWordRepository

class WordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            val learnedWordRepository = LearnedWordRepository(context)
            val availableWordRepository = AvailableWordRepository(context)
            return WordViewModel(learnedWordRepository, availableWordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SyncViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            val availableWordRepository = AvailableWordRepository(context)
            return SyncViewModel(availableWordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
