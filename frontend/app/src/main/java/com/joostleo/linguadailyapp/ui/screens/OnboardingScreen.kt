package com.joostleo.linguadailyapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joostleo.linguadailyapp.navigation.NavigationDestinations
import com.joostleo.linguadailyapp.ui.theme.Playfair
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.joostleo.linguadailyapp.R
import com.joostleo.linguadailyapp.utils.preferences.PreferencesManager

data class OnboardingPage(
    val icon: ImageVector? = null,
    val imageRes: Int? = null, // Add this for image resource
    val title: String? = null,
    val description: String,
    val iconColor: Color = Color(0xFF1F565E)
)

@Composable
fun OnboardingScreen(
    navController: NavController,
    onComplete: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF7F7F7)
    val accentColor = Color(0xFF1F565E)
    val lightBeige = Color(0xFFF7E5BE)

    // Define onboarding pages - easily modifiable
    val onboardingPages = listOf(
        OnboardingPage(
            icon = Icons.Default.Book,
            title = "Welcome to LinguaDaily",
            description = "Learn a new word every day and expand your vocabulary with our carefully curated language learning experience."
        ),
        OnboardingPage(
            imageRes = R.drawable.howtouse, // Replace with your actual image resource
            description = "Discover new words with detailed meanings, pronunciations, and example sentences to help you understand context."
        ),
        OnboardingPage(
            imageRes = R.drawable.lang, // Replace with your actual image resource
            description = "Choose your target language by tapping the flag button. You’ll receive multiple new daily words tailored to the language you’re learning!"
        ),
        OnboardingPage(
            imageRes = R.drawable.bottomnav, // Replace with your actual image resource
            description = "Navigate easily using the bottom bar. Bookmark words, browse your full word list, and get a new random word anytime with a single tap!"
        ),
        OnboardingPage(
            imageRes = R.drawable.dailycha2, // Replace with your actual image resource
            description = "Test your memory and reinforce learning with fun daily challenges. One small step every day adds up to big progress!"
        ),
//        OnboardingPage(
//            icon = Icons.Default.EmojiEvents,
//            title = "Track Your Progress",
//            description = "Monitor your learning journey with detailed statistics and celebrate your achievements as you master new words."
//        )
    )

    val context = LocalContext.current
    val prefs = PreferencesManager(context)

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Animation for the entire screen
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Scaffold(
        containerColor = backgroundColor,
        content = { paddingValues ->
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, easing = FastOutSlowInEasing))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Text(
                                text = "Get Started",
                                fontFamily = Playfair,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = Color.Black
                            )

                            Text(
                                text = "Learn how to make the most of your language journey",
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        // Main Content Card with Horizontal Pager
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 24.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                OnboardingPageContent(
                                    page = onboardingPages[page],
                                    lightBeige = lightBeige
                                )
                            }
                        }

                        // Bottom Navigation
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 20.dp)
                        ) {
                            // Page Indicators
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 24.dp)
                            ) {
                                repeat(onboardingPages.size) { index ->
                                    val isSelected = pagerState.currentPage == index
                                    val alpha by animateFloatAsState(
                                        targetValue = if (isSelected) 1f else 0.3f,
                                        animationSpec = tween(300),
                                        label = "indicator_alpha"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(if (isSelected) 12.dp else 8.dp)
                                            .background(
                                                color = accentColor,
                                                shape = CircleShape
                                            )
                                            .alpha(alpha)
                                    )
                                }
                            }

                            // Navigation Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Skip button
                                if (pagerState.currentPage < onboardingPages.size - 1) {
                                    TextButton(
                                        onClick = {
                                                prefs.setOnboardingCompleted(true)
                                            navController.navigate(NavigationDestinations.Home.route) {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = "Skip",
                                            color = Color.Gray,
                                            fontSize = 16.sp
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.width(64.dp))
                                }

                                // Next/Get Started button
                                Button(
                                    onClick = {
                                        if (pagerState.currentPage < onboardingPages.size - 1) {
                                            // Navigate to next page
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                            }
                                        } else {
                                            // Complete onboarding
                                            prefs.setOnboardingCompleted(true)
                                            navController.navigate(NavigationDestinations.Home.route) {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = accentColor
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text(
                                        text = if (pagerState.currentPage < onboardingPages.size - 1) "Next" else "Get Started",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

fun onComplete(onboardingCompleted: Unit) {

}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    lightBeige: Color
) {
    var isContentVisible by remember { mutableStateOf(false) }

    // Get screen configuration for responsive design
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val isSmallScreen = screenHeight < 700.dp // Adjust threshold as needed

    // Dynamic values based on screen size
    val imageHeight = if (isSmallScreen) 150.dp else 280.dp
    val iconSize = if (isSmallScreen) 70.dp else 120.dp
    val iconInnerSize = if (isSmallScreen) 40.dp else 64.dp
    val titleSize = if (isSmallScreen) 18.sp else 24.sp
    val descriptionSize = if (isSmallScreen) 13.sp else 16.sp
    val verticalSpacing = if (isSmallScreen) 8.dp else 16.dp
    val padding = if (isSmallScreen) 12.dp else 32.dp

    LaunchedEffect(Unit) {
        delay(200)
        isContentVisible = true
    }

    AnimatedVisibility(
        visible = isContentVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it / 2 },
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(600))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon/Image container
            if (page.imageRes != null) {
                // Display image instead of icon - responsive sizing
                Image(
                    painter = painterResource(id = page.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .width(imageHeight)
                        .height(imageHeight)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else if (page.icon != null) {
                // Display icon as before - responsive sizing
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .background(lightBeige, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = page.iconColor,
                        modifier = Modifier.size(iconInnerSize)
                    )
                }
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            // Title
            page.title?.let {
                Text(
                    text = it,
                    fontFamily = Playfair,
                    fontWeight = FontWeight.Bold,
                    fontSize = titleSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = (titleSize.value + 4).sp
                )
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            // Description
            Text(
                text = page.description,
                fontFamily = FontFamily.Default,
                fontSize = descriptionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (descriptionSize.value + 6).sp,
                modifier = Modifier.padding(horizontal = if (isSmallScreen) 8.dp else 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    MaterialTheme {
        OnboardingScreen(
            navController = rememberNavController(),
            onComplete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageContentPreview() {
    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            OnboardingPageContent(
                page = OnboardingPage(
                    icon = Icons.Default.Book,
                    title = "Welcome to LinguaDaily",
                    description = "Learn a new word every day and expand your vocabulary with our carefully curated language learning experience."
                ),
                lightBeige = Color(0xFFF7E5BE)
            )
        }
    }
}