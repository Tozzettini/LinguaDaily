package com.example.linguadailyapp.database.streakmanager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

// First, create a SharedPreferences helper class
object StreakCounter {
    private const val PREFS_NAME = "streak_prefs"
    private const val LAST_OPENED_KEY = "last_opened_date"
    private const val STREAK_COUNT_KEY = "streak_count"

    // Get current streak and update it
    fun updateStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = LocalDate.now().toEpochDay()

        val lastOpenedDay = prefs.getLong(LAST_OPENED_KEY, 0)
        val currentStreak = prefs.getInt(STREAK_COUNT_KEY, 0)

        // Calculate new streak based on last opened day
        val newStreak = when {
            lastOpenedDay == today -> currentStreak // Already opened today
            lastOpenedDay == today - 1 -> currentStreak + 1 // Opened yesterday
            lastOpenedDay == 0L -> 1 // First time opening app
            else -> 1 // Reset streak if missed a day
        }

        // Save the updated values
        prefs.edit()
            .putLong(LAST_OPENED_KEY, today)
            .putInt(STREAK_COUNT_KEY, newStreak)
            .apply()

        return newStreak
    }

}