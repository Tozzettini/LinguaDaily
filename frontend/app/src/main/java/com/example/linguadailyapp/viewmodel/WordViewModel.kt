package com.example.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.database.learnedWord.LearnedWordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class WordViewModel(private val learnedWordRepository: LearnedWordRepository, private val availableWordRepository: AvailableWordRepository) : ViewModel() {

    private val _words = MutableStateFlow<List<LearnedWord>>(emptyList())
    val words: StateFlow<List<LearnedWord>> = _words

    private val _todaysLearnedWord = MutableStateFlow<LearnedWord?>(null)
    val todaysLearnedWord: StateFlow<LearnedWord?> = _todaysLearnedWord

    private val _bookmarkedWords = MutableStateFlow<List<LearnedWord>>(emptyList())
    val bookmarkedWords: StateFlow<List<LearnedWord>> = _bookmarkedWords

    init {
        viewModelScope.launch {
            var todaysWord = learnedWordRepository.getTodaysWord()

            if(todaysWord == null) {
                todaysWord = getRandomWordBlocking(isWordOfDay = true)
            }
            _todaysLearnedWord.value = todaysWord

            learnedWordRepository.getAllWordsFlow().collect { allWords ->
                _words.value = allWords
                _bookmarkedWords.value = allWords.filter { word -> word.bookmarked }
            }
        }
    }

    suspend fun getLearnedWordById(id : Int): LearnedWord? {
        return learnedWordRepository.getWordById(id)
    }

    fun toggleBookmark(learnedWord: LearnedWord) {
        viewModelScope.launch {
            val updatedWord = learnedWord.copy(bookmarked = !learnedWord.bookmarked, bookmarkedAt = LocalDateTime.now())
            learnedWordRepository.updateWord(updatedWord)
        }
    }


    suspend fun getRandomWordBlocking(isWordOfDay: Boolean = false) : LearnedWord? {
        val randomWord = availableWordRepository.getRandomWord()

        if(randomWord == null) return randomWord

        val learnedWord = LearnedWord.of(availableWord = randomWord, isWordOfTheDay = isWordOfDay)

        learnedWordRepository.insert(learnedWord)
        availableWordRepository.removeWord(randomWord)

        return learnedWord
    }
}
