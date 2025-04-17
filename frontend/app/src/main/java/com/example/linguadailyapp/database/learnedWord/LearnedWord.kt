package com.example.linguadailyapp.database.learnedWord

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "learnedWords")
data class LearnedWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val description: String,
    val language: String,
    val learnedAt: LocalDate = LocalDate.now(),
    val etymology: String,
    val exampleSentence: String,
    val partOfSpeech: String,
    val phoneticSpelling: String,
    val bookmarked: Boolean = false,
    val bookmarkedAt: LocalDateTime? = LocalDateTime.now(),
    val isWordOfTheDay: Boolean = false
)
