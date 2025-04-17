package com.example.linguadailyapp.utils.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.linguadailyapp.R
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.utils.PreferencesManager

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
