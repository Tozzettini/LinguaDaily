package com.joostleo.linguadailyapp.utils.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.joostleo.linguadailyapp.R
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.utils.preferences.PreferencesManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit


fun sendNotification(title: String, message: String, context: Context) {
    if(!PreferencesManager(context).isNotificationsEnabled()) return

    val notificationBuilder = NotificationCompat.Builder(context, "LinguaDailyChannel")
        .setSmallIcon(R.drawable.ic_icon_v1)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = NotificationManagerCompat.from(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    notificationManager.notify(1, notificationBuilder.build()) // 1 is the notification ID
}

fun sendDailyNotification(learnedWord: LearnedWord, context: Context) {
    if(!PreferencesManager(context).isNotificationsEnabled()) return

    val notificationBuilder = NotificationCompat.Builder(context, "LinguaDailyChannel")
        .setSmallIcon(R.drawable.ic_icon_v1)
        .setContentTitle("Today's Word: ${learnedWord.word}")
        .setContentText(learnedWord.description)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = NotificationManagerCompat.from(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    notificationManager.notify(2, notificationBuilder.build()) // 1 is the notification ID
}

fun queueNotification(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
        .addTag("daily_notification")
        .build()

    val workManager = WorkManager.getInstance(context)

////    commented this out
//
//    val workInfos = workManager.getWorkInfosByTag("daily_notification").get()
////
//  if (workInfos.isNullOrEmpty()) {
//        workManager.enqueue(workRequest)
//    }
}



private fun getInitialDelay(): Long {
    val now = LocalDateTime.now()
    val targetTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(12, 0))
    val delay = Duration.between(now, targetTime)
    return if (delay.isNegative) {
        Duration.between(now, targetTime.plusDays(1)).toMillis()
    } else {
        delay.toMillis()
    }
}
