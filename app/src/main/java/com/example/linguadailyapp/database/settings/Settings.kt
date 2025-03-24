package com.example.linguadailyapp.database.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val lastSynced: LocalDate = LocalDate.MIN
)