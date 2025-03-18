package com.example.linguadailyapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.ui.screens.HomeScreen
import com.example.linguadailyapp.ui.screens.SettingsScreen
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import android.Manifest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create notif channel
        enableEdgeToEdge()
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

@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current

    // Register the permission request
    val requestPermissionLauncher =
        (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // If permission is granted, navigate to the HomeScreen
                onPermissionGranted()
            } else {
                // Show a toast or handle the case where permission is denied
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    // Request the permission for sending notifications
    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
}