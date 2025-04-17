package com.example.linguadailyapp.utils

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    // ---- Default values ----
    private val DEFAULT_NOTIFICATIONS_ENABLED = false
    private val DEFAULT_SYNC_ON_DATA = true
    private val DEFAULT_FIRST_LAUNCH = true
    private val DEFAULT_OUT_OF_WORDS = true
    private val DEFAULT_SKIP = 0

    // ---- Methods to access settings ----

    fun setSkip(skip: Int) {
        sharedPreferences.edit()
            .putInt("skip", skip)
            .apply()
    }

    fun getSkip(): Int {
        return sharedPreferences.getInt("skip", DEFAULT_SKIP)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean("notifications_enabled", enabled)
            .apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", DEFAULT_NOTIFICATIONS_ENABLED)
    }

    fun setAllowSyncOnData(value: Boolean) {
        sharedPreferences.edit()
            .putBoolean("sync_on_data", value)
            .apply()
    }

    fun getSyncAllowedOnData(): Boolean {
        return sharedPreferences.getBoolean("sync_on_data", DEFAULT_SYNC_ON_DATA)
    }

    fun isFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", DEFAULT_FIRST_LAUNCH)

        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
        }

        return isFirstLaunch
    }

    fun setOutOfWordsMode(value: Boolean) {
        sharedPreferences.edit()
            .putBoolean("is_out_of_words", value)
            .apply()
    }

    fun getOutOfWordsMode() : Boolean {
        return sharedPreferences.getBoolean("is_out_of_words", DEFAULT_OUT_OF_WORDS)
    }

}
