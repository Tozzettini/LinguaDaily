package com.example.linguadailyapp.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.components.SwitchSetting
import com.example.linguadailyapp.utils.PreferencesManager
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.example.linguadailyapp.utils.notification.NotificationPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var isNotificationsEnabled by rememberSaveable  { mutableStateOf(preferencesManager.isNotificationsEnabled()) }  // Initial state for notifications
    var allowSyncOnData by rememberSaveable  { mutableStateOf(preferencesManager.getSyncAllowedOnData()) }

    var showSettingsRedirect by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            preferencesManager.setNotificationsEnabled(true)
            isNotificationsEnabled = true
        } else {
            showSettingsRedirect = true
            preferencesManager.setNotificationsEnabled(false)
            isNotificationsEnabled = false
        }
    }

    val notificationPermission = NotificationPermission(context, permissionLauncher)

    MaterialTheme (colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate(NavigationDestinations.Home.route) // Fallback if no previous screen
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
    //                verticalArrangement = Arrangement.Center
                ) {
                    SwitchSetting(
                        label = "Enable Notifications",
                        isChecked = isNotificationsEnabled,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                if (notificationPermission.hasNotificationPermission()) {
                                    preferencesManager.setNotificationsEnabled(true)
                                    isNotificationsEnabled = true
                                } else {
                                    notificationPermission.relaunch()
                                }
                            } else {
                                preferencesManager.setNotificationsEnabled(false)
                                isNotificationsEnabled = false
                            }
                        }

                    )
                    SwitchSetting(
                        label = "Allow Mobile Data Usage",
                        isChecked = allowSyncOnData,
                        onCheckedChange = {
                            preferencesManager.setAllowSyncOnData(it)
                            allowSyncOnData = it
                        }
                    )
                }
            }
        )

        if (showSettingsRedirect) {
            AlertDialog(
                onDismissRequest = { showSettingsRedirect = false },
                title = { Text("Permission Blocked") },
                text = { Text("You've permanently denied notification access. Please enable it manually in app settings.") },
                confirmButton = {
                    TextButton(onClick = {
                        showSettingsRedirect = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }) { Text("Open Settings") }
                },
                dismissButton = {
                    TextButton(onClick = { showSettingsRedirect = false }) { Text("Cancel") }
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController()) // Provide a mock NavController for the preview
}