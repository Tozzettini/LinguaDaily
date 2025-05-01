package com.example.linguadailyapp.ui.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color

/**
 * Shows a Compose AlertDialog when there's no internet connection for watching ads
 */
@Composable
fun NoWifiPopupForAds(
    showDialog: MutableState<Boolean>,
    context: Context,
    onDismiss: () -> Unit = {}
) {
    if (showDialog.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                showDialog.value = false
                onDismiss()
            },
            title = { Text("No Internet Connection") },
            text = { Text("You need an internet connection to watch ads. Please connect to Wi-Fi or mobile data.") },
            confirmButton = {
                TextButton(

                    onClick = {
                        // Open WiFi settings
                        openWifiSettings(context)
                        showDialog.value = false
                    }
                ) {
                    Text("Wi-Fi Settings", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(

                    onClick = {
                        showDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text(text ="Cancel", color = Color.Black)
                }
            }
        )
    }
}

/**
 * Opens the WiFi settings on the device
 */
fun openWifiSettings(context: Context) {
    try {
        val wifiSettingsIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
        wifiSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(wifiSettingsIntent)
    } catch (e: Exception) {
        // Fallback to general settings if WiFi settings aren't available
        try {
            val settingsIntent = Intent(Settings.ACTION_SETTINGS)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(settingsIntent)
        } catch (e: Exception) {
            // Silently fail if even general settings can't be opened
        }
    }
}

// Example usage in your Composable:
/*
@Composable
fun YourScreen() {
    val showNoWifiPopup = remember { mutableStateOf(false) }

    // Your existing UI

    // Add this to your Composable
    NoWifiPopupForAds(
        showDialog = showNoWifiPopup,
        context = LocalContext.current
    )

    // Then in your button click handler:
    onWatchAdClick = {
        val hasConnection = ConnectionManager.getNetworkType(context) != NetworkType.NONE

        if(hasConnection) {
            activity?.let {
                RewardedAdManager.showAd(it) {
                    cooldownManager.resetCooldown()
                    wordViewModel.updateState()
                    showCooldownModal = false
                }
            }
        } else {
            showNoWifiPopup.value = true
        }
    }
}
*/