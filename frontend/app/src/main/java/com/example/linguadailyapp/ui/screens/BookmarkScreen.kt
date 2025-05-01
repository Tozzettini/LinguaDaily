package com.example.linguadailyapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.datamodels.LearnedWord
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.components.LinguaBottomNavigation
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.ui.theme.Playfair
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
import java.time.format.DateTimeFormatter
import java.util.Locale




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    navController: NavController,
    viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val bookmarkedWords by viewModel.bookmarkedWords.collectAsState()

    val backgroundColor = Color(0xFFF7F7F7)
    val accentColor = Color(0xFF1F565E)
    val primaryBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF7F5F0),  // Light cream color
            Color(0xFFF1E4D2),  // Slightly darker cream
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Bookmarks",
                        fontFamily = Playfair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(NavigationDestinations.Home.route)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                ),
                modifier = Modifier
                    .drawBehind {
                        // Draw a thin top border
                        drawLine(
                            color = Color(0xFFE0E0E0),
                            start = Offset(0f, size.height),  // Start at bottom-left
                            end = Offset(size.width, size.height),  // End at bottom-right
                            strokeWidth = 2.dp.toPx()
                        )
                    }
            )
        },
        bottomBar = {
            LinguaBottomNavigation(navController = navController)
        },
        content = { paddingValues ->
            if (bookmarkedWords.isEmpty()) {
                EmptyBookmarksView(Modifier.padding(paddingValues))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(primaryBackground)
                        .padding(paddingValues)
                ) {
                    BookmarkGrid(
                        learnedWords = bookmarkedWords,
                        navController = navController,
                        onTrashClicked = { word -> viewModel.toggleBookmark(word) }
                    )
                }
            }
        }
    )
}

@Composable
fun EmptyBookmarksView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = Color(0xFF1F565E).copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No bookmarks yet",
                fontSize = 20.sp,
                fontFamily = Playfair,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Words you bookmark will appear here",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = FontFamily.Default
            )
        }
    }
}

@Composable
fun BookmarkGrid(
    learnedWords: List<LearnedWord>,
    navController: NavController,
    onTrashClicked: (LearnedWord) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(learnedWords) { word ->
            BookmarkCard(word, navController, onTrashClicked)
        }
    }
}

@Composable
fun BookmarkCard(learnedWord: LearnedWord, navController: NavController, onTrashClicked: (LearnedWord) -> Unit) {
    val accentColor = Color(0xFF1F565E) // Dark teal

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { navController.navigate(NavigationDestinations.Word.createRoute(learnedWord.id)) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with date and delete icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)
                val formattedDate = learnedWord.bookmarkedAt?.format(formatter) ?: "Unknown date"

                Text(
                    text = formattedDate,
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                // Delete/Unbookmark button with animation similar to WordCard bookmark button
                Box {
                    // Animate background color
                    val backgroundColor by animateColorAsState(
                        targetValue = Color(0xFF1F565E),
                        label = "BackgroundColorAnimation"
                    )

                    // Animate icon tint
                    val iconTint by animateColorAsState(
                        targetValue = Color.White,
                        label = "IconTintAnimation"
                    )

                    // Pop animation on toggle
                    val scale by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "IconScaleAnimation"
                    )

                    IconButton(
                        onClick = { onTrashClicked(learnedWord) },
                        modifier = Modifier
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                            .size(32.dp)
                            .background(
                                color = backgroundColor,
                                shape = CircleShape
                            )
                            .padding(horizontal = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove bookmark",
                            tint = iconTint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Word
            Text(
                text = learnedWord.word,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Playfair,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Description
            Text(
                text = learnedWord.description,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.Black.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookmarkScreen() {
    val navController = rememberNavController()
    LinguaDailyAppTheme {
        BookmarkScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookmarkCard() {
    val navController = rememberNavController()

    LinguaDailyAppTheme {
        BookmarkCard(
            LearnedWord(
                word = "Serendipity",
                description = "The occurrence and development of events by chance in a happy or beneficial way",
                language = Language.ENGLISH,
                exampleSentence = "",
                phoneticSpelling = "",
                etymology = "",
                partOfSpeech = ""

            ),
            navController = navController,
            onTrashClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyBookmarksView() {
    LinguaDailyAppTheme {
        EmptyBookmarksView()
    }
}