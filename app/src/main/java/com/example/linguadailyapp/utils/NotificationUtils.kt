package com.example.linguadailyapp.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.linguadailyapp.R

const val CHANNEL_ID = "default_channel"

fun sendNotification(context: Context) {
    // Create Notification Channel (only if running on Android Oreo or higher)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Build the notification
    val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
        //.setSmallIcon(R.drawable.ic_notification)  // Add your icon here
        .setContentTitle("New Notification")
        .setContentText("This is a sample notification!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // Get Notification Manager and trigger notification
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1, notification)  // 1 is the notification ID
}
