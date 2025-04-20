package com.example.linguadailyapp.utils.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.example.linguadailyapp.viewmodel.WordViewModel

class DailyNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val wordViewModel = WordViewModel(learnedWordRepository = LearnedWordRepository(applicationContext), availableWordRepository = AvailableWordRepository(applicationContext))
        val todaysWord = wordViewModel.getTodaysLearnedWord()

        if(todaysWord != null) {
            sendDailyNotification(todaysWord, applicationContext)
        }

        return Result.success()
    }


}
