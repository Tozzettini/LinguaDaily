package com.example.linguadailyapp.database.availableword

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "availableWords")
data class AvailableWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val description: String,
    val language: String,
    val etymology: String,
    val exampleSentence: String,
    val partOfSpeech: String,
    val phoneticSpelling: String
)
