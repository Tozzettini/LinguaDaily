package com.example.linguadailyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.ui.screens.BookmarkScreen
import com.example.linguadailyapp.ui.screens.DailyChallengeScreen
import com.example.linguadailyapp.ui.screens.HomeScreen
import com.example.linguadailyapp.ui.screens.SettingsScreen
import com.example.linguadailyapp.ui.screens.WordDetailScreen
import com.example.linguadailyapp.ui.screens.WordDetailScreen
import com.example.linguadailyapp.ui.screens.WordsScreen

class AppNavigation {

    @Composable
    fun start() {
        val navController = rememberNavController()

        // Define NavHost with Start Destination
        NavHost(navController = navController, startDestination = NavigationDestinations.Home.route) {
            composable(NavigationDestinations.Home.route) { HomeScreen(navController) }
            composable(NavigationDestinations.Settings.route) { SettingsScreen(navController) }
            composable(NavigationDestinations.WordsList.route) { WordsScreen(navController) }
            composable(NavigationDestinations.Bookmark.route) { BookmarkScreen(navController) }
            composable(NavigationDestinations.Challenge.route) { DailyChallengeScreen(navController) }
            composable(NavigationDestinations.Word.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    WordDetailScreen(navController, id)
                }
            }

        }
    }




}