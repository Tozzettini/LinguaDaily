package com.example.linguadailyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

open class VocabularyStatsViewModel(application: Application) : AndroidViewModel(application) {

    // Preferences manager for persistent storage
    private val preferencesManager = PreferencesManager(application)

    // Data structure for vocabulary stats
    data class VocabularyStats(
        val wordsLearned: Int = 0,
        val totalGoal: Int = 365,  // Default yearly goal
        val streakDays: Int = 0,
        val lastOpenedDate: LocalDate? = null
    )

    // StateFlow to observe from UI
    private val _stats = MutableStateFlow(
        VocabularyStats(
            wordsLearned = preferencesManager.getWordsLearned(),
            totalGoal = preferencesManager.getTotalGoal(),
            streakDays = preferencesManager.getStreakDays(),
            lastOpenedDate = preferencesManager.getLastOpenedDate()
        )
    )
    val stats: StateFlow<VocabularyStats> = _stats.asStateFlow()

    init {
        // Update streak when ViewModel is initialized
        updateStreak()
    }

    /**
     * Updates the word count based on the actual number of words in the user's collection
     */
    fun updateWordsLearned(wordCount: Int) {
        viewModelScope.launch {
            _stats.update { currentStats ->
                currentStats.copy(wordsLearned = wordCount)
            }
            preferencesManager.setWordsLearned(wordCount)
        }
    }

    /**
     * Updates the user's yearly vocabulary goal
     */
    fun updateTotalGoal(goal: Int) {
        viewModelScope.launch {
            _stats.update { currentStats ->
                currentStats.copy(totalGoal = goal)
            }
            preferencesManager.setTotalGoal(goal)
        }
    }

    /**
     * Updates the user's streak based on app usage
     */
    private fun updateStreak() {
        val today = LocalDate.now()
        val lastOpened = _stats.value.lastOpenedDate

        if (lastOpened == null) {
            // First time using the app
            _stats.update { it.copy(streakDays = 1, lastOpenedDate = today) }
            preferencesManager.setStreakDays(1)
            preferencesManager.setLastOpenedDate(today)
            return
        }

        val daysBetween = ChronoUnit.DAYS.between(lastOpened, today)

        when {
            daysBetween == 0L -> {
                // Already opened today, no streak change
            }
            daysBetween == 1L -> {
                // Consecutive day, increment streak
                val newStreak = _stats.value.streakDays + 1
                _stats.update { it.copy(streakDays = newStreak, lastOpenedDate = today) }
                preferencesManager.setStreakDays(newStreak)
                preferencesManager.setLastOpenedDate(today)
            }
            else -> {
                // Streak broken, reset to 1
                _stats.update { it.copy(streakDays = 1, lastOpenedDate = today) }
                preferencesManager.setStreakDays(1)
                preferencesManager.setLastOpenedDate(today)
            }
        }
    }

    /**
     * Manually increment streak (can be used for testing)
     */
    fun incrementStreak() {
        val currentStreak = _stats.value.streakDays
        val newStreak = currentStreak + 1

        _stats.update { it.copy(streakDays = newStreak) }
        preferencesManager.setStreakDays(newStreak)
    }

    /**
     * Reset stats (for testing or user account reset)
     */
    fun resetStats() {
        _stats.update {
            VocabularyStats(
                wordsLearned = 0,
                totalGoal = 365,
                streakDays = 0,
                lastOpenedDate = null
            )
        }

        preferencesManager.setWordsLearned(0)
        preferencesManager.setTotalGoal(365)
        preferencesManager.setStreakDays(0)
        preferencesManager.setLastOpenedDate(null)
    }
}