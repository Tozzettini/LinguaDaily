package com.joostleo.linguadailyapp.utils.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.joostleo.linguadailyapp.MainActivity
import com.joostleo.linguadailyapp.R
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.utils.preferences.PreferencesManager
import android.app.AlarmManager
import android.util.Log
import java.time.LocalTime
import java.util.Calendar


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

fun sendDailyNotification(learnedWords: List<LearnedWord>, context: Context) {
    if(!PreferencesManager(context).isNotificationsEnabled()) return

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(context, "LinguaDailyChannel")
        .setSmallIcon(R.drawable.ic_icon_v1)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    when (learnedWords.size) {
        0 -> return

        1 -> {
            val word = learnedWords.first()
            notificationBuilder
                .setContentTitle("Today's Word: ${word.word}")
                .setContentText(word.description)
        }

        in 2..3 -> {
            val words = learnedWords.joinToString(", ") { it.word }
            notificationBuilder
                .setContentTitle("Today's Words: $words")
                .setContentText("Tap here to check them out!")
        }

        else -> {
            val words = learnedWords.take(3).joinToString(", ") { it.word }
            notificationBuilder
                .setContentTitle("Today's Words: $words...")
                .setContentText("Tap here to check them out!")
        }
    }

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

fun scheduleDailyNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, DailyNotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance()

    // 60 minutes - (difference in minutes from 12:00)
    val minutes = 60 - LocalTime.now().minute

    // 24 hours - (difference in hours from 12:00) - (1 hour to compensate for the minutes)
    val hours = 35 - LocalTime.now().hour

    calendar.apply {
        add(Calendar.HOUR, hours)
        add(Calendar.MINUTE, minutes)
    }

    alarmManager.setAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

    Log.d("NotificationUtils", "Scheduled Alarm")

}

