package com.example.linguadailyapp.utils

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // ---- Default values ----
    private val DEFAULT_LAST_SYNCED = LocalDateTime.MIN
    private val DEFAULT_NOTIFICATIONS_ENABLED = false

    // ---- Methods to access settings ----

    // Save and retrieve LocalDateTime
    fun setLastSynced(value: LocalDateTime) {
        sharedPreferences.edit()
            .putString("last_synced", value.format(formatter))
            .apply()
    }

    fun getLastSynced(): LocalDateTime {
        val storedValue = sharedPreferences.getString("last_synced", null)
        return storedValue?.let { LocalDateTime.parse(it, formatter) } ?: DEFAULT_LAST_SYNCED
    }

    // Save and retrieve notification preference
    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean("notifications_enabled", enabled)
            .apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", DEFAULT_NOTIFICATIONS_ENABLED)
    }
}
