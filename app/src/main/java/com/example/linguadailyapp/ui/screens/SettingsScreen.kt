package com.example.linguadailyapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.getDarkModeSetting
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.saveDarkModeSetting
import com.example.linguadailyapp.ui.components.SwitchSetting
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.sendNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    var isNotificationsEnabled by rememberSaveable  { mutableStateOf(true) }  // Initial state for notifications

    var isDarkmode by rememberSaveable { mutableStateOf(getDarkModeSetting(context)) }  // Retrieve persisted dark mode setting

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
                        onCheckedChange = { isNotificationsEnabled = it }                )
                    //--
                    SwitchSetting(
                        label = "Enable Dark mode",
                        isChecked = isDarkmode,
                        onCheckedChange = { newDarkModeSetting ->
                            // Save the new dark mode setting to SharedPreferences
                            saveDarkModeSetting(context, newDarkModeSetting)
                            isDarkmode = newDarkModeSetting

                        }
                    )
                    //--
                    val context = LocalContext.current
                    ElevatedButton(
                        onClick = { sendNotification("You clicked the button", "so we sent a notification", context) },
                        modifier = Modifier.align(CenterHorizontally)
                    )
                    {
                        Text(text = "Send me a test-notification" )
                    }



                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController()) // Provide a mock NavController for the preview
}