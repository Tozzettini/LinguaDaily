package com.joostleo.linguadailyapp.utils.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.LanguagePreferencesManager
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModel
import com.joostleo.linguadailyapp.viewmodel.WordViewModel

class DailyNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val wordViewModel = WordViewModel(
            learnedWordRepository = LearnedWordRepository(applicationContext),
            availableWordRepository = AvailableWordRepository(applicationContext),
            wordSyncLogic = WordSyncLogic(AvailableWordRepository(applicationContext)),
            languageViewModel = LanguageViewModel(LanguagePreferencesManager(applicationContext)),
            cooldownManager = RandomWordCooldownManager(applicationContext))
        val languageViewModel = LanguageViewModel(LanguagePreferencesManager(applicationContext))

        val languages = languageViewModel.getSelectedLanguages()
        val todayWords = wordViewModel.getTodaysLearnedWords(languages)

        if(todayWords.isNotEmpty()) sendDailyNotification(todayWords, applicationContext)

        return Result.success()
    }

}
