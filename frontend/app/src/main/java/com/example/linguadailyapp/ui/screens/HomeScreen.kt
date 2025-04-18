package com.example.linguadailyapp.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.ui.components.*
import com.example.linguadailyapp.ui.components.DailyChallengeCard
import com.example.linguadailyapp.ui.components.VocabularyStatsWidget
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.viewmodel.ImprovedStyledTopBar2
import com.example.linguadailyapp.viewmodel.LanguageViewModel
import com.example.linguadailyapp.viewmodel.LanguageViewModelFactory
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
import kotlinx.coroutines.delay
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val todaysWord by viewModel.todaysLearnedWord.collectAsState()
    var currentLanguage by remember { mutableStateOf(Languagetype("English", "en")) }

    // Get the application context
    val context = LocalContext.current

    // Get SharedPreferences instance
    val sharedPreferences = remember {
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    // Create the ViewModel using the factory
    val viewModel: LanguageViewModel = viewModel(
        factory = LanguageViewModelFactory(sharedPreferences)
    )

    // Animation states
    var isHeaderVisible by remember { mutableStateOf(false) }
    var isStatsVisible by remember { mutableStateOf(false) }
    var isChallengeVisible by remember { mutableStateOf(false) }
    var isWordCardVisible by remember { mutableStateOf(false) }
    var isWordCardVisible2 by remember { mutableStateOf(false) }
    var isWordCardVisible3 by remember { mutableStateOf(false) }


    // Staggered animation
    LaunchedEffect(Unit) {
        delay(100)
        isHeaderVisible = true
        delay(200)
        isStatsVisible = true
        delay(200)
        isChallengeVisible = true
        delay(200)
        isWordCardVisible = true
        delay(250)
        isWordCardVisible2 = true
        delay(300)
        isWordCardVisible3 = true
    }

    // Background gradients
    val primaryBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF7F5F0),  // Light cream color
            Color(0xFFF1E4D2),  // Slightly darker cream
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    Scaffold(
        topBar = {
            // Use our new improved top bar
//            ImprovedStyledTopBar(
//                navController = navController,
//                onLanguagesSelected = { selectedLangs ->
//                    // Store your selected languages here
//                    // For example, update a ViewModel or save to preferences
////                    viewModel.updateSelectedLanguages(selectedLangs)
//                }
//            )

            ImprovedStyledTopBar2(
                navController = navController,
                viewModel = viewModel
            )
        },
        bottomBar = {
                LinguaBottomNavigation(navController = navController)
                    },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryBackground)
            ) {
                // Main content column with scroll
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Welcome header
                    AnimatedVisibility(
                        visible = isHeaderVisible,
                        enter = fadeIn(animationSpec = tween(500)) +
                                slideInVertically(
                                    initialOffsetY = { -it },
                                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                )
                    ) {
                        DailyInspirationBanner()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats widget
                    AnimatedVisibility(
                        visible = isStatsVisible,
                        enter = fadeIn(animationSpec = tween(500)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                )
                    ) {
                        VocabularyStatsWidget()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily challenge
                    AnimatedVisibility(
                        visible = isChallengeVisible,
                        enter = fadeIn(animationSpec = tween(500)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                )
                    ) {
                        DailyChallengeCard(navController = navController)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Animated main word card
                    AnimatedVisibility(
                        visible = isWordCardVisible,
                        enter = fadeIn(animationSpec = tween(700)) +
                                slideInVertically(
                                    initialOffsetY = { it / 3 },
                                    animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
                                )
                    ) {
                        MainWordCard2(learnedWord = todaysWord?: LearnedWord.default(), navController = navController)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Animated main word card2
                    AnimatedVisibility(
                        visible = isWordCardVisible2,
                        enter = fadeIn(animationSpec = tween(900)) +
                                slideInVertically(
                                    initialOffsetY = { it / 5 },
                                    animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
                                )
                    ) {
                        MainWordCard2(navController = navController)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Animated main word card3
                    AnimatedVisibility(
                        visible = isWordCardVisible3,
                        enter = fadeIn(animationSpec = tween(1100)) +
                                slideInVertically(
                                    initialOffsetY = { it / 5 },
                                    animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
                                )
                    ) {
                        MainWordCard2(navController = navController)
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    )
}
fun getTimeBasedGreeting(): String {
    val currentHour = LocalTime.now().hour

    return when (currentHour) {
        in 5..11 -> "Good Morning princess!"
        in 12..16 -> "Good Afternoon!"
        in 17..21 -> "Good Evening!"
        else -> "Wow you're up late!"
    }
}
@Composable
fun DailyInspirationBanner() {
    val bannerColor = Color(0xFF1F565E).copy(alpha = 0.08f)
    val todaysWord = "minatory"
    val greeting = getTimeBasedGreeting()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(containerColor = bannerColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Lightbulb,
                contentDescription = "Inspiration",
                tint = Color(0xFF1F565E),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1F565E)
                )
                Text(
                    text = "Word of the Day: ${todaysWord}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1F565E).copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Small phone", device = "spec:width=311dp,height=491dp")
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    LinguaDailyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(750.dp)
        ) {
            HomeScreen(navController = navController)
        }
    }
}