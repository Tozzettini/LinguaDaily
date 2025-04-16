package com.example.linguadailyapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.ConnectionManager
import com.example.linguadailyapp.utils.notification.DailyNotificationWorker
import com.example.linguadailyapp.utils.NetworkType
import com.example.linguadailyapp.utils.notification.NotificationPermission
import com.example.linguadailyapp.utils.PreferencesManager
import com.example.linguadailyapp.utils.notification.sendNotification
import com.example.linguadailyapp.viewmodel.SyncViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val preferencesManager = PreferencesManager(this)

        if(preferencesManager.isFirstLaunch()) {
            NotificationPermission(
                this,
                registerForActivityResult(ActivityResultContracts.RequestPermission())
                { isGranted ->
                    handlePermissionResult(this, isGranted)
                }
            ).init()
        }

        //Starts with false since system inital setting is Lightmode
        var isDarkmode = getDarkModeSetting(this)

        var syncViewModel = SyncViewModel(
            WordRepository(this@MainActivity)
        )

        if(preferencesManager.getSyncAllowedOnData() || (ConnectionManager.getNetworkType(this) == NetworkType.WIFI)) {
            syncViewModel.sync(preferencesManager)
        }


        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        setContent {
            LinguaDailyAppTheme {
                AppNavigation().start()
            }
        }
    }
}

fun handlePermissionResult(context: Context, isGranted: Boolean) {
    if ( !isGranted ) {
        Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
    } else {
        PreferencesManager(context).setNotificationsEnabled(true)
        sendNotification("You enabled notifications!", "we are just checking they actually work ;)", context)
    }
}

fun saveDarkModeSetting(context: Context, isDarkMode: Boolean) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    with(sharedPreferences.edit()) {
        putBoolean("dark_mode", isDarkMode)

        apply()
    }

}

fun getDarkModeSetting(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("dark_mode", false) // Default to light mode
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