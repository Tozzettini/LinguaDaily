package com.example.linguadailyapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.datamodels.LearnedWord
import com.example.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.utils.WordSyncLogic
import com.example.linguadailyapp.utils.preferences.RandomWordCooldownManager
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

    init {
        viewModelScope.launch {
            languageViewModel.selectedLanguages.collect { selectedLanguages ->
                // When selectedLanguages changes, reload the today's learned words
                _todaysLearnedWords.value = getTodaysLearnedWords(selectedLanguages)
                _randomWordState.value = getRandomWordState()

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
            _randomWordState.value = getRandomWordState()
        }
    }

    suspend fun getRandomWordState() : RandomWordState {
        if(cooldownManager.isInCooldown.value) return RandomWordState.COOLDOWN

        if(!wordSyncLogic.canSyncInBackground()) return RandomWordState.SYNC_NEEDED

        return RandomWordState.IDLE

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

    private suspend fun filterLanguagesByAvailableWords(languages: Set<Language>) : List<Language> =
        languages.filter { availableWordRepository.getWordCountForLanguage(it) != 0 }

    suspend fun getRandomWordBlocking(isWordOfDay: Boolean = false, languages: Set<Language>) : LearnedWord? {
        val filteredLanguages = filterLanguagesByAvailableWords(languages)

        if(filteredLanguages.isEmpty()) return null

        val randomWord = availableWordRepository.getRandomWordForLanguage(filteredLanguages.random())

        if(randomWord == null) return randomWord

        val learnedWord = LearnedWord.of(availableWord = randomWord, isWordOfTheDay = isWordOfDay)

        learnedWordRepository.insert(learnedWord)
        availableWordRepository.removeWord(randomWord)

        return learnedWord
    }

    suspend fun getTodaysLearnedWordForLanguage(language: Language) : LearnedWord? {
        var todaysWord = learnedWordRepository.getTodaysWordForLanguage(language)

        if(todaysWord == null) {
            todaysWord = getRandomWordBlocking(isWordOfDay = true, setOf(language))
        }

        return todaysWord
    }

    suspend fun getTodaysLearnedWords(languages: Set<Language>) : List<LearnedWord> {
        val list = mutableListOf<LearnedWord>()

        for(language in languages) {
            val word = getTodaysLearnedWordForLanguage(language)

            if(word != null) list.add(word)
        }

        return list
    }


}
