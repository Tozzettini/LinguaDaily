package com.example.linguadailyapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.ui.screens.LoadingScreen
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.ConnectionManager
import com.example.linguadailyapp.utils.notification.DailyNotificationWorker
import com.example.linguadailyapp.utils.NetworkType
import com.example.linguadailyapp.utils.notification.NotificationPermission
import com.example.linguadailyapp.utils.PreferencesManager
import com.example.linguadailyapp.utils.notification.sendNotification
import com.example.linguadailyapp.viewmodel.SyncViewModel
import kotlinx.coroutines.runBlocking
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

        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        val syncViewModel = SyncViewModel(WordRepository(this))
        val wordRepository = WordRepository(this)
        val context = this

        setContent {
            LinguaDailyAppTheme {
                var appReady by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if(wordRepository.getWordCount() != 0) {
                        syncViewModel.sync(preferencesManager)
                        appReady = true
                        return@LaunchedEffect
                    }

                    val networkType = ConnectionManager.getNetworkType(context)

                    if ( networkType == NetworkType.WIFI || (networkType == NetworkType.MOBILE_DATA && preferencesManager.getSyncAllowedOnData()) )  {
                        runBlocking {
                            syncViewModel.syncBlocking(preferencesManager)
                        }
                        preferencesManager.setOutOfWordsMode(false)
                        appReady = true
                        return@LaunchedEffect
                    }

                    showNoInternetDialog(
                        context = context,
                        allowSyncOnData = preferencesManager.getSyncAllowedOnData(),
                        preferencesManager = preferencesManager,
                        onCancelled = {
                            preferencesManager.setOutOfWordsMode(true)
                        },
                        onSyncTriggered = {
                            val updatedNetworkType = ConnectionManager.getNetworkType(context)

                            if (preferencesManager.getSyncAllowedOnData() || updatedNetworkType == NetworkType.WIFI) {
                                runBlocking {
                                    syncViewModel.syncBlocking(preferencesManager)
                                }
                                preferencesManager.setOutOfWordsMode(false)

                            } else {
                                preferencesManager.setOutOfWordsMode(true)

                                showNoInternetDialog(
                                    context,
                                    preferencesManager.getSyncAllowedOnData(),
                                    preferencesManager,
                                    onCancelled = {
                                        preferencesManager.setOutOfWordsMode(true)
                                    },
                                    onSyncTriggered = {
                                        runBlocking {
                                            syncViewModel.syncBlocking(preferencesManager)
                                        }
                                    }
                                )
                            }
                            appReady = true
                        }
                    )
                }

                if (appReady) {
                    AppNavigation().start()
                } else {
                    LoadingScreen()
                }
            }
        }
    }
}

private fun showNoInternetDialog(
    context: Context,
    allowSyncOnData: Boolean,
    preferencesManager: PreferencesManager,
    onCancelled: () -> Unit,
    onSyncTriggered: () -> Unit
) {
    val builder = AlertDialog.Builder(context)
        .setTitle("No Internet Connection")
        .setMessage("To sync new words, please connect to Wi-Fi or allow syncing over mobile data.")
        .setCancelable(false)

        .setNegativeButton("Cancel") { _, _ ->
            preferencesManager.setOutOfWordsMode(true)
            onCancelled()
        }

        .setPositiveButton("Wi-Fi Settings") { _, _ ->
            context.startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
            onSyncTriggered()
        }

    if (!allowSyncOnData) {
        builder.setNeutralButton("Allow Mobile Data") { _, _ ->
            preferencesManager.setAllowSyncOnData(true)

            val networkType = ConnectionManager.getNetworkType(context)
            if (networkType == NetworkType.MOBILE_DATA || networkType == NetworkType.WIFI) {
                onSyncTriggered()
            } else {
                android.os.Handler(context.mainLooper).postDelayed({
                    showNoInternetDialog(
                        context,
                        allowSyncOnData = true,
                        preferencesManager,
                        onCancelled,
                        onSyncTriggered
                    )
                }, 300)
            }
        }
    }

    builder.show()
}



private fun handlePermissionResult(context: Context, isGranted: Boolean) {
    if ( !isGranted ) {
        Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
    } else {
        PreferencesManager(context).setNotificationsEnabled(true)
        sendNotification("You're Daily words are:", "List of daily words in diffrent languages;)", context)
    }
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