package com.example.linguadailyapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.theme.Playfair
import com.example.linguadailyapp.utils.PreferencesManager
import com.example.linguadailyapp.utils.notification.NotificationPermission
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var isNotificationsEnabled by rememberSaveable { mutableStateOf(preferencesManager.isNotificationsEnabled()) }
    var allowSyncOnData by rememberSaveable { mutableStateOf(preferencesManager.getSyncAllowedOnData()) }
    var showSettingsRedirect by rememberSaveable { mutableStateOf(false) }

    val backgroundColor = Color(0xFFF7F7F7)
    val accentColor = Color(0xFF1F565E)
    val lightBeige = Color(0xFFF7E5BE)

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

    // Animation states
    var isSettingVisable0 by remember { mutableStateOf(false) }
    var isSettingVisable1 by remember { mutableStateOf(false) }





    // Staggered animation
    LaunchedEffect(Unit) {
        delay(100)
        isSettingVisable0 = true
        delay(200)
        isSettingVisable1 = true

    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontFamily = Playfair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(NavigationDestinations.Home.route)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(lightBeige, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "App Preferences",
                            fontFamily = Playfair,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        Text(
                            text = "Customize your language learning experience",
                            fontFamily = FontFamily.Default,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Settings Items
                AnimatedVisibility(
                    visible = isSettingVisable0,
                    enter = fadeIn(animationSpec = tween(500)) +
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                            )
                ) {

                    SettingItem(
                        icon = if (isNotificationsEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                        title = "Daily Word Notifications",
                        description = "Receive notifications for your daily word",
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
                }

                AnimatedVisibility(
                    visible = isSettingVisable1,
                    enter = fadeIn(animationSpec = tween(500)) +
                            slideInVertically(
                                initialOffsetY = { it/2 },
                                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                            )
                ) {

                    SettingItem(
                        icon = Icons.Default.DataUsage,
                        title = "Allow Mobile Data Usage",
                        description = "Download new words using mobile data",
                        isChecked = allowSyncOnData,
                        onCheckedChange = {
                            preferencesManager.setAllowSyncOnData(it)
                            allowSyncOnData = it
                        }
                    )
                }
            }
        }
    )

    if (showSettingsRedirect) {
        AlertDialog(
            onDismissRequest = { showSettingsRedirect = false },
            containerColor = Color.White,
            titleContentColor = accentColor,
            textContentColor = Color.Black,
            title = {
                Text(
                    "Permission Required",
                    fontFamily = Playfair,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "You've denied notification access. Please enable it manually in app settings to receive daily word notifications.",
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsRedirect = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text(
                        "Open Settings",
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsRedirect = false }) {
                    Text(
                        "Cancel",
                        color = Color.Gray
                    )
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val accentColor = Color(0xFF1F565E)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Color(0xFFF7E5BE),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isChecked) accentColor else accentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = accentColor,
                    checkedBorderColor = accentColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.LightGray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingItemPreview() {
    MaterialTheme {
        SettingItem(
            icon = Icons.Default.NotificationsActive,
            title = "Daily Word Notifications",
            description = "Receive notifications for your daily word",
            isChecked = true,
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(navController = rememberNavController())
    }
}