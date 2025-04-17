package com.example.linguadailyapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.linguadailyapp.navigation.NavigationDestinations



@Preview
@Composable
fun LinguaBottomNavigationPreview() {

    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}


@Composable
fun LinguaBottomNavigation(navController: NavController) {
    // Define navigation items
    val items = listOf(
        NavigationItem(
            name = "Words",
            route = NavigationDestinations.WordsList.route,
            icon = Icons.Default.Lightbulb,
            contentDescription = "Words List"
        ),
        NavigationItem(
            name = "Home",
            route = NavigationDestinations.Home.route,
            icon = Icons.Default.Home,
            contentDescription = "Home"
        ),
        NavigationItem(
            name = "Bookmarks",
            route = NavigationDestinations.Bookmark.route,
            icon = Icons.Default.Bookmarks,
            contentDescription = "Bookmarks"
        ),
        NavigationItem(
            name = "Random",
            route = "random_word", // You'll need to create this route
            icon = Icons.Default.Shuffle,
            contentDescription = "Random Word"
        )
    )

    // Primary brand color for active items
    val brandColor = Color(0xFF1F565E)

    // Current route state
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = brandColor,
        tonalElevation = 8.dp,
        modifier = Modifier.height(64.dp)
            .drawBehind {
                // Draw a thin top border
                drawLine(
                    color = Color(0xFFE0E0E0),  // Light gray border
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            // Animate color changes for a smoother experience
            val iconColor by animateColorAsState(
                targetValue = if (selected) brandColor else Color.Gray.copy(alpha = 0.6f),
                label = "iconColor"
            )

            NavigationBarItem(
                icon = {
                    Column {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.name,
                        fontSize = 12.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = selected,
                onClick = {
                    if (item.route == "random_word") {
                        // Add logic to navigate to a random word
                        // For example, fetch a random word ID first, then navigate
                        // viewModel.getRandomWordId { wordId ->
                        //     navController.navigate("word_details/$wordId")
                        // }
                    } else {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to avoid
                            // building up a large stack of destinations
                            popUpTo(NavigationDestinations.Home.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = brandColor,
                    selectedTextColor = brandColor,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
                    unselectedTextColor = Color.Gray.copy(alpha = 0.6f)
                )
            )
        }
    }
}

// Data class for navigation items
data class NavigationItem(
    val name: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val contentDescription: String
)