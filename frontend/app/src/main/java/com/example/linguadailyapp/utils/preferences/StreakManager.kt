package com.example.linguadailyapp.utils.preferences

import android.content.Context
import java.time.LocalDate
import androidx.core.content.edit

/**
 * A utility class to manage daily streak counts.
 * Tracks when the user last opened the app and calculates streak accordingly.
 */
object StreakCounter {
    private const val PREFS_NAME = "streak_prefs"
    private const val LAST_OPENED_KEY = "last_opened_date"
    private const val STREAK_COUNT_KEY = "streak_count"

    /**
     * Gets the current streak count without updating it.
     * Useful for just displaying the current streak.
     */
    fun getCurrentStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(STREAK_COUNT_KEY, 0)
    }

    /**
     * Updates the streak based on the current date and last opened date.
     * Returns the updated streak count.
     */
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
        prefs.edit() {
            putLong(LAST_OPENED_KEY, today)
                .putInt(STREAK_COUNT_KEY, newStreak)
        }

        return newStreak
    }

    /**
     * Manually resets the streak to zero and clears the last opened date.
     * Useful for testing or when user wants to reset their progress.
     */
    fun resetStreak(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit() {
            putLong(LAST_OPENED_KEY, 0)
                .putInt(STREAK_COUNT_KEY, 0)
        }
    }

    /**
     * For testing: Updates streak with a specific date instead of the current date
     */
    internal fun updateStreakWithDate(context: Context, date: LocalDate): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val targetDay = date.toEpochDay()

        val lastOpenedDay = prefs.getLong(LAST_OPENED_KEY, 0)
        val currentStreak = prefs.getInt(STREAK_COUNT_KEY, 0)

        val newStreak = when {
            lastOpenedDay == targetDay -> currentStreak // Already opened today
            lastOpenedDay == targetDay - 1 -> currentStreak + 1 // Opened yesterday
            lastOpenedDay == 0L -> 1 // First time opening app
            else -> 1 // Reset streak if missed a day
        }

        prefs.edit()
            .putLong(LAST_OPENED_KEY, targetDay)
            .putInt(STREAK_COUNT_KEY, newStreak)
            .apply()

        return newStreak
    }
}