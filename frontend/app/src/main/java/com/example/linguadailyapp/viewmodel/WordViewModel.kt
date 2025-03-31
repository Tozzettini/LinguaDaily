package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.database.word.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words

    private val _todaysWord = MutableStateFlow(Word(word = "", description = "", language = "", date = LocalDate.MIN))
    val todaysWord: StateFlow<Word> = _todaysWord

    private val _bookmarkedWords = MutableStateFlow<List<Word>>(emptyList())
    val bookmarkedWords: StateFlow<List<Word>> = _bookmarkedWords

    init {
        viewModelScope.launch {
            repository.getAllWordsFlow().collect { allWords ->
                _words.value = allWords
                val today = LocalDate.now()
                _todaysWord.value = allWords.firstOrNull { it.date == today } ?: Word(word = "Ciao", description = "Hello", language = "Italian", date = LocalDate.now())
                _bookmarkedWords.value = allWords.filter { word -> word.bookmarked }
            }
        }
    }

    fun toggleBookmark(word: Word) {
        viewModelScope.launch {
            val updatedWord = word.copy(bookmarked = !word.bookmarked, bookmarkedAt = LocalDateTime.now())
            repository.updateWord(updatedWord)
        }
    }
}
