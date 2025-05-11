package com.joostleo.linguadailyapp.utils

import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.datamodels.Language
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager
import com.joostleo.linguadailyapp.viewmodel.RandomWordState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RandomWordLogic(
    private val availableWordRepository: AvailableWordRepository,
    private val learnedWordRepository: LearnedWordRepository,
    private val cooldownManager: RandomWordCooldownManager,
    private val wordSyncLogic: WordSyncLogic
) {

    private val mutex = Mutex()

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

    suspend fun getTodaysLearnedWordForLanguage(language: Language): LearnedWord? {
        return mutex.withLock {
            var todaysWord = learnedWordRepository.getTodaysWordForLanguage(language)

            if (todaysWord == null) {
                todaysWord = getRandomWordBlocking(isWordOfDay = true, setOf(language))
            }

            todaysWord
        }
    }


    suspend fun getTodaysLearnedWords(languages: Set<Language>) : List<LearnedWord> {
        val list = mutableListOf<LearnedWord>()

        for(language in languages) {
            val word = getTodaysLearnedWordForLanguage(language)

            if(word != null) list.add(word)
        }

        return list
    }


    suspend fun filterLanguagesByAvailableWords(languages: Set<Language>) : List<Language> =
        languages.filter { availableWordRepository.getWordCountForLanguage(it) != 0 }

    suspend fun getRandomWordState() : RandomWordState {
        if(cooldownManager.isInCooldown.value) return RandomWordState.COOLDOWN

        if(!wordSyncLogic.canSyncInBackground()) return RandomWordState.SYNC_NEEDED

        return RandomWordState.IDLE

    }

}