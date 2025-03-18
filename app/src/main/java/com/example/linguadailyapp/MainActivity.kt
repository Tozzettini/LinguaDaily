package com.example.linguadailyapp


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

        setContent {
            LinguaDailyAppTheme {
                AppNavigation()
            }
        }
    }
}

// move this to navigation package?
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Define NavHost with Start Destination
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
