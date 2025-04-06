package com.example.linguadailyapp.utils.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NotificationPermission(
    private val context: Context,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    fun init() {
        createNotificationChannel()

        if(hasNotificationPermission()) {
           return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as ComponentActivity, Manifest.permission.POST_NOTIFICATIONS)) {
            return
        }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    fun relaunch() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "LinguaDailyChannel",
                "Lingua Daily Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun hasNotificationPermission() : Boolean {
       return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
               ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}
