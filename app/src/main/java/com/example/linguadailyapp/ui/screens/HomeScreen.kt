package com.example.linguadailyapp.ui.screens


import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.ui.components.ButtonBanner
import com.example.linguadailyapp.ui.components.TopBar
import com.example.linguadailyapp.navigation.NavigationDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLanguageIconClick: () -> Unit = {},
    onSettingsIconClick: () -> Unit = { }
) {



    Scaffold(
        topBar = {
            TopBar(
                onLanguageIconClick = {
                    // Show the language pop-up
//                    isLanguagePopupVisible = true
                },
                onSettingsIconClick = {
                    // Navigate to the settings screen
                    navController.navigate(NavigationDestinations.Settings.route)
                }
            )
        },
        content = { paddingValues ->
            // Main content of the HomeScreen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
                    {

                        Spacer(modifier = Modifier.height(16.dp))
                        ButtonBanner(navController = navController)
                        // Add your home screen content here
                        Text(
                            text = "Welcome to the Daily Word App!",
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController() // ✅ Correct way to provide a NavController in preview
    MaterialTheme {
        HomeScreen(
            navController = navController
        )
    }
}