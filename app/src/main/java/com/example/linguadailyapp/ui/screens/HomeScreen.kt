package com.example.linguadailyapp.ui.screens

import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguadailyapp.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLanguageIconClick: () -> Unit = {},
    onSettingsIconClick: () -> Unit = {}
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
//                    navController.navigate("settings")
                }
            )
        },
        content = { paddingValues ->
            // Main content of the HomeScreen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
                    {
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
    MaterialTheme {
        HomeScreen(
            onLanguageIconClick = {
                // Handle back navigation in the preview
            },
            onSettingsIconClick = {
                // Handle menu icon click in the preview
            }
        )
    }
}