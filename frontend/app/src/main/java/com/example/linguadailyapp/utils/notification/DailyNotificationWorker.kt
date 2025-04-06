package com.example.linguadailyapp.utils.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.linguadailyapp.database.word.WordRepository

class DailyNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Get data from Room database
        val wordRepository = WordRepository(applicationContext)
        val todaysWord = wordRepository.getTodaysWord()

        if(todaysWord != null) {
            sendDailyNotification(todaysWord, applicationContext)
        }

        return Result.success()
    }


}
