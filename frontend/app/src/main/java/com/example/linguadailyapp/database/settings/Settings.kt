package com.example.linguadailyapp.database.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val lastSynced: Instant = Instant.MIN
)