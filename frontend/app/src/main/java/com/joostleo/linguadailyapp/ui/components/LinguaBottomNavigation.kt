package com.joostleo.linguadailyapp.ui.components

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.joostleo.linguadailyapp.database.availableword.AvailableWordRepository
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordRepository
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.navigation.NavigationDestinations
import com.joostleo.linguadailyapp.utils.ConnectionManager
import com.joostleo.linguadailyapp.utils.NetworkType
import com.joostleo.linguadailyapp.utils.RandomWordLogic
import com.joostleo.linguadailyapp.utils.WordSyncLogic
import com.joostleo.linguadailyapp.utils.preferences.PreferencesManager
import com.joostleo.linguadailyapp.utils.preferences.RandomWordCooldownManager
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModel
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModelFactory
import com.joostleo.linguadailyapp.viewmodel.RandomWordState
import com.joostleo.linguadailyapp.viewmodel.RewardedAdManager
import com.joostleo.linguadailyapp.viewmodel.SyncViewModel
import com.joostleo.linguadailyapp.viewmodel.SyncViewModelFactory
import com.joostleo.linguadailyapp.viewmodel.WordViewModel
import com.joostleo.linguadailyapp.viewmodel.WordViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Preview
@Composable
fun LinguaBottomNavigationPreview() {
    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}

@Preview(
    name = "Phone - Normal",
    device = "spec:width=411dp,height=891dp,orientation=portrait"
)
@Composable
fun BottomNav_Preview_Phone() {
    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}


@Preview(
    name = "Tablet - Landscape",
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun BottomNav_Preview_Tablet() {
    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}
@Preview(
    name = "Small Phone",
    device = "spec:width=320dp,height=640dp,orientation=portrait"
)
@Composable
fun BottomNav_Preview_SmallPhone() {
    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}

@Preview(
    name = "Tall Phone (Gesture Nav)",
    device = "spec:width=411dp,height=1000dp,orientation=portrait"
)
@Composable
fun BottomNav_Preview_TallPhone() {
    LinguaBottomNavigation(navController = NavController(LocalContext.current))
}




@Composable
fun LinguaBottomNavigation(
    navController: NavController,
    wordViewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current)),
    languageViewModel: LanguageViewModel = viewModel(factory = LanguageViewModelFactory(LocalContext.current)),
    syncViewModel: SyncViewModel = viewModel(factory = SyncViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val cooldownManager = remember { RandomWordCooldownManager(context) }
    val randomWordLogic = RandomWordLogic(
        availableWordRepository = AvailableWordRepository(context),
        learnedWordRepository = LearnedWordRepository(context),
        cooldownManager = cooldownManager,
        wordSyncLogic = WordSyncLogic(AvailableWordRepository(context)),
        )

    var showCooldownModal by remember { mutableStateOf(false) }
    var showSyncDialog by remember { mutableStateOf(false) }

    val randomWordState by wordViewModel.randomWordState.collectAsState()
    val languagesSelected by languageViewModel.selectedLanguages.collectAsState()

    val preferencesManager = PreferencesManager(context)

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    val navigationBarHeight = if (screenHeight <= 640) 64.dp else 64.dp
    val navigationIconSize = if (screenHeight <= 640) 20.dp else 24.dp
    val navigationTextSize = if (screenHeight <= 640) 10.sp else 12.sp


    // Update cooldown status every second if modal is showing
    LaunchedEffect(showCooldownModal) {
        if (showCooldownModal) {
            while (true) {
                cooldownManager.calculateCooldownStatus()
                delay(1000) // Update every second

                // If cooldown is over, hide the modal
                if (!cooldownManager.isInCooldown.value) {
                    wordViewModel.updateState()
                    showCooldownModal = false
                    break
                }
            }
        }
    }

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
            route = "random_word",
            icon = Icons.Default.Shuffle,
            contentDescription = "Random Word"
        )
    )

    // Primary brand color for active items
    val brandColor = Color(0xFF1F565E)
    val hasConnection = ConnectionManager.getNetworkType(context) != NetworkType.NONE

    // Current route state
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val activity = context as? Activity

// Load ad once on entry
    LaunchedEffect(true) {
        RewardedAdManager.loadAd(context)
    }
    val showNoWifiPopup = remember { mutableStateOf(false) }

    // Your existing UI

    // Add this to your Composable
    NoWifiPopupForAds(
        showDialog = showNoWifiPopup,
        context = LocalContext.current
    )

    // Show cooldown modal if needed and display ad
    if (showCooldownModal) {
        RandomWordCooldownModal(
            isVisible = true,
            remainingTime = cooldownManager.remainingTime.value,
            onWatchAdClick = {

                if(hasConnection) {
                    activity?.let {
                        RewardedAdManager.showAd(it) {
                            // Reset cooldown after ad is watched
//                            cooldownManager.resetCooldown()
                            wordViewModel.updateState()
                            showCooldownModal = false
                            //---F

                            if (randomWordState == RandomWordState.COOLDOWN) {
                                showCooldownModal = true
                            }

                            var randomWord: LearnedWord? = null

                            runBlocking {
                                randomWord = randomWordLogic.getRandomWordBlocking(languages = languagesSelected)

                                if(syncViewModel.shouldSync()) syncViewModel.syncInBackground(PreferencesManager(context))
                            }

                            if (randomWord != null) {
                                wordViewModel.updateState()
                                navController.navigate(NavigationDestinations.Word.createRoute(
                                    randomWord!!.id))
                            }
                        }
                    }
                } else {
                    showNoWifiPopup.value = true
                }
            },
            onDismiss = {
                showCooldownModal = false
            }
        )
    }

    //                // Reset cooldown after ad is watched
//                cooldownManager.resetCooldown()
//                wordViewModel.updateState()
//                showCooldownModal = false


    if(showSyncDialog) {
        outOfRandomWordsDialog(
            context = context,
            allowSyncOnData = preferencesManager.getSyncAllowedOnData(),
            onCancelled = {
                wordViewModel.updateState()
                showSyncDialog = false
            },
            onSyncTriggered = {
                runBlocking {
                    syncViewModel.syncBlocking(preferencesManager)
                }

                wordViewModel.updateState()

                showSyncDialog = false
            },
            preferencesManager = preferencesManager
        )
    }

    NavigationBar(
        containerColor = Color.White,
        contentColor = brandColor,
        tonalElevation = 8.dp,
        modifier = Modifier
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

            // Calculate if the random button should be disabled
            val isRandomDisabled = item.route == "random_word" && cooldownManager.isInCooldown.value

            NavigationBarItem(
                icon = {
                    Column (horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.padding(top = 2.dp)// adjust this for spacing
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                            tint = if (isRandomDisabled) Color.Gray.copy(alpha = 0.3f) else iconColor,
                            modifier = Modifier.size(navigationIconSize)
                        )
                        Text(
                            text = item.name,
                            fontSize = navigationTextSize,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isRandomDisabled) Color.Gray.copy(alpha = 0.3f) else Color.Unspecified
                        )
                    }
                },
//                label = {
//                  used to use labels here but there was too much spacing
//                },
                selected = selected,
                onClick = {

                    if (item.route == "random_word") {
                        // Check if random word button is in cooldown
                        if(randomWordState == RandomWordState.SYNC_NEEDED) {
                            showSyncDialog = true
                            return@NavigationBarItem
                        }

                        if (randomWordState == RandomWordState.COOLDOWN) {
                            showCooldownModal = true
                            return@NavigationBarItem
                        }

                        val canClick = cooldownManager.incrementClickCount()

                        if (canClick) {
                            // Handle first click - get random word
                            var randomWord: LearnedWord? = null

                            runBlocking {
                                randomWord = randomWordLogic.getRandomWordBlocking(languages = languagesSelected)

                                if(syncViewModel.shouldSync()) syncViewModel.syncInBackground(PreferencesManager(context))
                            }

                            if (randomWord != null) {
                                wordViewModel.updateState()
                                navController.navigate(NavigationDestinations.Word.createRoute(
                                    randomWord!!.id))
                            }


                        } else {
                            // Handle second click - show cooldown modal
                            showCooldownModal = true
                        }
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
                ),
                enabled = true
            )
        }
    }
}

private fun outOfRandomWordsDialog(
    context: Context,
    allowSyncOnData: Boolean,
    preferencesManager: PreferencesManager,
    onCancelled: () -> Unit,
    onSyncTriggered: () -> Unit
) {
    val message =
        if(allowSyncOnData) "To get new words, please connect to either Wi-Fi or mobile data."
        else "To get new words, please connect to Wi-Fi or allow syncing over mobile data."

    val builder = AlertDialog.Builder(context)
        .setTitle("We have run out of words")
        .setMessage(message)
        .setCancelable(false)

        .setNegativeButton("Cancel") { _, _ ->
            preferencesManager.setOutOfWordsMode(true)
            onCancelled()
        }

        .setPositiveButton("Retry") { _, _ ->
            onSyncTriggered()
        }

    if (!allowSyncOnData) {
        builder.setNeutralButton("Allow Mobile Data") { _, _ ->
            preferencesManager.setAllowSyncOnData(true)

            val networkType = ConnectionManager.getNetworkType(context)
            if (networkType == NetworkType.MOBILE_DATA || networkType == NetworkType.WIFI) {
                onSyncTriggered()
            } else {
                Handler(context.mainLooper).postDelayed({
                    outOfRandomWordsDialog(
                        context,
                        allowSyncOnData = true,
                        preferencesManager,
                        onCancelled,
                        onSyncTriggered
                    )
                }, 300)
            }
        }
    }

    builder.show()
}

// Data class for navigation items
data class NavigationItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
)