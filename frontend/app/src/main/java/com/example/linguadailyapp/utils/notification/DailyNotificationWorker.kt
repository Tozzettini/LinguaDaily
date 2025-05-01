package com.example.linguadailyapp.utils.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.example.linguadailyapp.datamodels.LearnedWord
import com.example.linguadailyapp.utils.WordSyncLogic
import com.example.linguadailyapp.utils.preferences.LanguagePreferencesManager
import com.example.linguadailyapp.utils.preferences.RandomWordCooldownManager
import com.example.linguadailyapp.viewmodel.LanguageViewModel
import com.example.linguadailyapp.viewmodel.WordViewModel

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
        val todayWord = wordViewModel.getTodaysLearnedWordForLanguage(languages.first())

        if(todayWord != null) sendDailyNotification(todayWord, applicationContext)

        return Result.success()
    }

}
