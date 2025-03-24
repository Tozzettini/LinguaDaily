package com.example.linguadailyapp.database.word

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val description: String,
    val language: String,
    val date: LocalDate
)
