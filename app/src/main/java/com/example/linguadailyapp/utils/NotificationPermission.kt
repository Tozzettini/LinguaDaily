package com.example.linguadailyapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NotificationPermission(
    private val context: Context,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    fun launch() {
        createNotificationChannel()
        checkNotificationPermission()
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

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as ComponentActivity, Manifest.permission.POST_NOTIFICATIONS)) {
            return
        }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    companion object {
        fun handlePermissionResult(context: Context, isGranted: Boolean) {
            if ( !isGranted ) {
                Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
