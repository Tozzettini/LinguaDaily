package com.joostleo.linguadailyapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "availableWords")
data class AvailableWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val translation: String,
    val description: String,
    val language: Language,
    val etymology: String,
    val exampleSentence: String,
    val partOfSpeech: String,
    val phoneticSpelling: String
)