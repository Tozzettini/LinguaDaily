package com.joostleo.linguadailyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.utils.RandomWordLogic
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

enum class RandomWordState {
    IDLE,
    COOLDOWN,
    SYNC_NEEDED
}

class WordViewModel(private val learnedWordRepository: LearnedWordRepository, private val availableWordRepository: AvailableWordRepository, private val languageViewModel: LanguageViewModel, private val wordSyncLogic: WordSyncLogic, private val cooldownManager: RandomWordCooldownManager) : ViewModel() {

    private val _words = MutableStateFlow<List<LearnedWord>>(emptyList())
    val words: StateFlow<List<LearnedWord>> = _words

    private val _todaysLearnedWords = MutableStateFlow<List<LearnedWord>>(emptyList())
    val todaysLearnedWords: StateFlow<List<LearnedWord>> = _todaysLearnedWords

    private val _bookmarkedWords = MutableStateFlow<List<LearnedWord>>(emptyList())
    val bookmarkedWords: StateFlow<List<LearnedWord>> = _bookmarkedWords

    private val _randomWordState = MutableStateFlow<RandomWordState>(RandomWordState.IDLE)
    val randomWordState = _randomWordState.asStateFlow()

    private val randomWordLogic = RandomWordLogic(availableWordRepository, learnedWordRepository, cooldownManager, wordSyncLogic)

    init {
        viewModelScope.launch {
            languageViewModel.selectedLanguages.collect { selectedLanguages ->
                // When selectedLanguages changes, reload the today's learned words
                _todaysLearnedWords.value = randomWordLogic.getTodaysLearnedWords(selectedLanguages)
                _randomWordState.value = randomWordLogic.getRandomWordState()

                // You might want to fetch or filter the words based on the new selected languages
                learnedWordRepository.getAllWordsFlow().collect { allWords ->
                    _words.value = allWords
                    _bookmarkedWords.value = allWords.filter { it.bookmarked }
                }
            }
        }
    }

    fun updateState() {
        viewModelScope.launch {
            _randomWordState.value = randomWordLogic.getRandomWordState()
        }
    }

    fun toggleBookmark(learnedWord: LearnedWord) {
        viewModelScope.launch {
            val updatedWord = learnedWord.copy(bookmarked = !learnedWord.bookmarked, bookmarkedAt = LocalDateTime.now())
            learnedWordRepository.updateWord(updatedWord)
        }
    }

    suspend fun getLearnedWordById(id : Int): LearnedWord? {
        return learnedWordRepository.getWordById(id)
    }


}
