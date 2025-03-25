package com.example.linguadailyapp.database.settings

import android.content.Context
import com.example.linguadailyapp.database.LocalDatabase
import java.time.LocalDate

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

    suspend fun getLastSynced() : LocalDate {
        return settingsDao.getSettings()?.lastSynced ?: LocalDate.MIN
    }

    suspend fun setLastSynced(date: LocalDate) {

        val current = currentSettings() ?: Settings()

        val updatedSettings = current.copy(
            lastSynced = date
        )

        settingsDao.saveSettings(updatedSettings)
    }
}
