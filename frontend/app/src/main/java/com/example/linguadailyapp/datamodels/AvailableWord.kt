package com.example.linguadailyapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.linguadailyapp.datamodels.Language

@Entity(tableName = "availableWords")
data class AvailableWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val description: String,
    val language: Language,
    val etymology: String,
    val exampleSentence: String,
    val partOfSpeech: String,
    val phoneticSpelling: String
)