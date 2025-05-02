package com.joostleo.linguadailyapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "learnedWords")
data class LearnedWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val description: String,
    val language: Language,
    val learnedAt: LocalDate = LocalDate.now(),
    val etymology: String,
    val exampleSentence: String,
    val partOfSpeech: String,
    val phoneticSpelling: String,
    val bookmarked: Boolean = false,
    val bookmarkedAt: LocalDateTime? = LocalDateTime.now(),
    val isWordOfTheDay: Boolean = false
) {
    companion object {
        fun of(
            availableWord: AvailableWord,
            learnedAt: LocalDate = LocalDate.now(),
            bookmarked: Boolean = false,
            bookmarkedAt: LocalDateTime? =  LocalDateTime.now(),
            isWordOfTheDay: Boolean = false
        ): LearnedWord {
            return LearnedWord(
                id = availableWord.id,
                word = availableWord.word,
                description = availableWord.description,
                language = availableWord.language,
                exampleSentence = availableWord.exampleSentence,
                phoneticSpelling = availableWord.phoneticSpelling,
                partOfSpeech = availableWord.partOfSpeech,
                etymology = availableWord.etymology,
                learnedAt = learnedAt,
                bookmarked = bookmarked,
                bookmarkedAt = bookmarkedAt,
                isWordOfTheDay = isWordOfTheDay
            )
        }
        fun default(): LearnedWord {
            return LearnedWord(
                id = -1,
                word = "Default",
                description = "No description available.",
                language = Language.ENGLISH,
                exampleSentence = "This is a default word.",
                phoneticSpelling = "[ˈdɪˌfɔlt]",
                partOfSpeech = "noun",
                etymology = "From Latin 'defallere'"
            )
        }

    }
}