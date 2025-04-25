package com.example.linguadailyapp.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.content.edit

class RandomWordCooldownManager(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "random_word_cooldown_prefs"
        private const val KEY_CLICK_COUNT = "click_count"
        private const val KEY_COOLDOWN_TIMESTAMP = "cooldown_timestamp"
        private const val KEY_LAST_CLICK_DATE = "last_click_date"
        private const val CLICK_LIMIT = 1 // 0-indexed, so 1 means 2 clicks
        private const val COOLDOWN_HOURS = 24
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // States that can be observed
    val isInCooldown = mutableStateOf(false)
    val remainingTime = mutableStateOf<Duration?>(null)

    init {
        // Check if we need to reset daily count
        resetIfNewDay()
        // Check if there's an active cooldown
        calculateCooldownStatus()
    }

    fun incrementClickCount(): Boolean {
        val currentCount = getClickCount()
        val newCount = currentCount + 1

        // Save the new count
        sharedPreferences.edit() { putInt(KEY_CLICK_COUNT, newCount) }

        // Save the timestamp of the last click
        sharedPreferences.edit() { putString(KEY_LAST_CLICK_DATE, getCurrentDateString()) }

        // Check if we've reached the limit
        if (newCount > CLICK_LIMIT) {
            startCooldown()
            isInCooldown.value = true
            calculateCooldownStatus()
            return false
        }

        return true
    }

    fun resetCooldown() {
        sharedPreferences.edit() {
            putInt(KEY_CLICK_COUNT, 0)
                .remove(KEY_COOLDOWN_TIMESTAMP)
        }

        isInCooldown.value = false
        remainingTime.value = null
    }

    fun calculateCooldownStatus() {
        val cooldownTimestamp = getCooldownTimestamp()
        if (cooldownTimestamp != null) {
            val now = LocalDateTime.now()
            val cooldownEnds = cooldownTimestamp.plusHours(COOLDOWN_HOURS.toLong())

            if (now.isBefore(cooldownEnds)) {
                isInCooldown.value = true
                remainingTime.value = Duration.between(now, cooldownEnds)
            } else {
                // Cooldown expired
                isInCooldown.value = false
                remainingTime.value = null
                resetCooldown()
            }
        } else {
            isInCooldown.value = false
            remainingTime.value = null
        }
    }

    private fun getClickCount(): Int {
        return sharedPreferences.getInt(KEY_CLICK_COUNT, 0)
    }

    private fun resetIfNewDay() {
        val lastClickDate = sharedPreferences.getString(KEY_LAST_CLICK_DATE, null)
        val currentDate = getCurrentDateString()

        if (lastClickDate != null && lastClickDate != currentDate) {
            // It's a new day, reset the click count
            sharedPreferences.edit() { putInt(KEY_CLICK_COUNT, 0) }
        }
    }

    private fun startCooldown() {
        sharedPreferences.edit() {
            putString(
                KEY_COOLDOWN_TIMESTAMP,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        }
    }

    private fun getCooldownTimestamp(): LocalDateTime? {
        val timestamp = sharedPreferences.getString(KEY_COOLDOWN_TIMESTAMP, null)
        return if (timestamp != null) {
            LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } else {
            null
        }
    }

    private fun getCurrentDateString(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}