package com.joostleo.linguadailyapp.navigation

sealed class NavigationDestinations(val route: String) {
    object Home : NavigationDestinations("home")
    object Settings : NavigationDestinations("settings")
    object WordsList : NavigationDestinations("wordsList")
    object Bookmark : NavigationDestinations("bookMark")
    object Challenge : NavigationDestinations("challenge")
    object Onboarding : NavigationDestinations("onboarding")
    object Word : NavigationDestinations("word/{id}") {
        fun createRoute(id: Int): String = "word/$id"
    }
}