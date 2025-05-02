package com.example.linguadailyapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.linguadailyapp.database.availableword.AvailableWordRepository
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.ui.screens.LoadingScreen
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.ConnectionManager
import com.example.linguadailyapp.utils.NetworkType
import com.example.linguadailyapp.utils.WordSyncLogic
import com.example.linguadailyapp.utils.notification.NotificationPermission
import com.example.linguadailyapp.utils.notification.queueNotification
import com.example.linguadailyapp.utils.preferences.PreferencesManager
import com.example.linguadailyapp.viewmodel.SyncViewModel
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.runBlocking

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

        queueNotification(this)

        val syncViewModel = SyncViewModel(WordSyncLogic(AvailableWordRepository(this)))
        val context = this
        val wordSyncLogic = WordSyncLogic(AvailableWordRepository(this))



        //ads - test device id: ABC123
        // Initialize AdMob SDK
        MobileAds.initialize(this) {}

// Set your actual test device ID
        val testDeviceIds = listOf("E00275ABA00DA3CA26368F6303D8347B") // Ziches phone ID
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()

        MobileAds.setRequestConfiguration(configuration)


        //---


        setContent {
            LinguaDailyAppTheme {
                var appReady by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if(wordSyncLogic.canSyncInBackground()) {
                        syncViewModel.syncInBackground(preferencesManager)
                        appReady = true
                        return@LaunchedEffect
                    }

                    val networkType = ConnectionManager.getNetworkType(context)

                    if ( networkType == NetworkType.WIFI || (networkType == NetworkType.MOBILE_DATA && preferencesManager.getSyncAllowedOnData()) )  {
                        runBlocking {
                            wordSyncLogic.syncBlockingForLanguages(preferencesManager)
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
                            appReady = true
                        },
                        onSyncTriggered = {
                            val updatedNetworkType = ConnectionManager.getNetworkType(context)

                            if (preferencesManager.getSyncAllowedOnData() || updatedNetworkType == NetworkType.WIFI) {
                                runBlocking {
                                    wordSyncLogic.syncBlockingForLanguages(preferencesManager)
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
                                            wordSyncLogic.syncBlockingForLanguages(preferencesManager)
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
        // Do nothing
    } else {
        PreferencesManager(context).setNotificationsEnabled(true)
    }
}