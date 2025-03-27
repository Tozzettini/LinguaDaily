package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.database.word.WordRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words

    private val _todaysWord = MutableStateFlow(Word(word = "", description = "", language = "", date = LocalDate.MIN))
    val todaysWord: StateFlow<Word> = _todaysWord

    init {
        viewModelScope.launch {
            val allWords = repository.getAllWords()
            _words.value = allWords

            val today = LocalDate.now()
            _todaysWord.value = allWords.firstOrNull { it.date == today }!!
        }
    }
}
