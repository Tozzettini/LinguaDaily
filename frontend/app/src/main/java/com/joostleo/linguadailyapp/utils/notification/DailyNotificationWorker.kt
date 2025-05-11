package com.joostleo.linguadailyapp.utils.notification

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.utils.RandomWordLogic
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.LanguagePreferencesManager
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModel
import com.joostleo.linguadailyapp.viewmodel.WordViewModel
import com.joostleo.linguadailyapp.viewmodel.WordViewModelFactory

class DailyNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val languageViewModel = LanguageViewModel(LanguagePreferencesManager(applicationContext))
        val randomWordLogic = RandomWordLogic(
            learnedWordRepository = LearnedWordRepository(applicationContext),
            availableWordRepository = AvailableWordRepository(applicationContext),
            wordSyncLogic = WordSyncLogic(AvailableWordRepository(applicationContext)),
            cooldownManager = RandomWordCooldownManager(applicationContext))

        val languages = languageViewModel.getSelectedLanguages()
        val todayWords = randomWordLogic.getTodaysLearnedWords(languages)

        if(todayWords.isNotEmpty()) sendDailyNotification(todayWords, applicationContext)

        return Result.success()
    }

}
