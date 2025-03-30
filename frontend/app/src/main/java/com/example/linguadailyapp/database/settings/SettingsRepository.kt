package com.example.linguadailyapp.database.settings

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import java.time.Instant
import java.time.LocalDateTime

class SettingsRepository(val context : Context) {

    private val settingsDao = LocalDatabase.getInstance(context).settingsDao()

    private suspend fun currentSettings() = settingsDao.getSettings()


    suspend fun hasDarkModeEnabled() : Boolean {
        return settingsDao.getSettings()?.darkModeEnabled ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {

        val current = currentSettings() ?: Settings()

        val updatedSettings = current.copy(
            darkModeEnabled = enabled
        )

        settingsDao.saveSettings(updatedSettings)
    }

    suspend fun hasNotificationsEnabled() : Boolean {
        return settingsDao.getSettings()?.notificationsEnabled ?: false
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {

        val current = currentSettings() ?: Settings()

        val updatedSettings = current.copy(
            notificationsEnabled = enabled
        )

        settingsDao.saveSettings(updatedSettings)
    }

    suspend fun getLastSynced() : Instant {
        return settingsDao.getSettings()?.lastSynced ?: Instant.MIN
    }

    suspend fun setLastSynced(datetime: Instant) {

        val current = currentSettings() ?: Settings()

        val updatedSettings = current.copy(
            lastSynced = datetime
        )

        settingsDao.saveSettings(updatedSettings)
    }
}
