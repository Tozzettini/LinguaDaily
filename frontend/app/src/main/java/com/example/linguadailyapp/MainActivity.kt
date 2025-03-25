package com.example.linguadailyapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.screens.HomeScreen
import com.example.linguadailyapp.ui.screens.SettingsScreen
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.NotificationPermission

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        NotificationPermission(
            this,
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted ->
                NotificationPermission.handlePermissionResult(this, isGranted)
            }
        ).launch()

        //Starts with false since system inital setting is Lightmode
        var isDarkmode = getDarkModeSetting(this)



        setContent {

            LinguaDailyAppTheme(darkTheme = isDarkmode) {
                AppNavigation().start()
            }
        }
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


