package com.joostleo.linguadailyapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joostleo.linguadailyapp.datamodels.Language
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.navigation.NavigationDestinations
import com.joostleo.linguadailyapp.ui.components.LinguaBottomNavigation
import com.joostleo.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.joostleo.linguadailyapp.ui.theme.Playfair
import com.joostleo.linguadailyapp.viewmodel.WordViewModel
import com.joostleo.linguadailyapp.viewmodel.WordViewModelFactory
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(
    navController: NavController,
    viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val words by viewModel.words.collectAsState()

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
                        "Words Collection",
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
            if (words.isEmpty()) {
                EmptyWordsView(Modifier.padding(paddingValues))
            } else {
                WordsList(
                    modifier = Modifier.padding(paddingValues),
                    learnedWords = words,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    )
}

@Composable
fun EmptyWordsView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = Color(0xFF1F565E).copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No words yet",
                fontSize = 20.sp,
                fontFamily = Playfair,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your daily words will appear here",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = FontFamily.Default
            )
        }
    }
}






//@Composable
//fun WordsList(
//    modifier: Modifier,
//    learnedWords: List<LearnedWord>,
//    viewModel: WordViewModel,
//    navController: NavController
//) {
//    val primaryBackground = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFFF7F5F0),
//            Color(0xFFF1E4D2),
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(0f, Float.POSITIVE_INFINITY)
//    )
//
//    val animatedItems = remember { mutableStateListOf<Int>() }
//    val animationPlayed = remember { mutableStateOf(false) }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(primaryBackground)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState()) // <- make Column scrollable
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            learnedWords.forEachIndexed { index, word ->
//
//                val isVisible = remember { mutableStateOf(false) }
//
//                LaunchedEffect(Unit) {
//                    if (!animationPlayed.value) {
//                        delay(index * 70L)
//                        isVisible.value = true
//                        animatedItems.add(index)
//
//                        if (index == learnedWords.lastIndex) {
//                            animationPlayed.value = true
//                        }
//                    } else {
//                        isVisible.value = true
//                    }
//                }
//
//                AnimatedVisibility(
//                    visible = isVisible.value,
//                    enter = slideInVertically(
//                        initialOffsetY = { it / 2 },
//                        animationSpec = tween(durationMillis = 300)
//                    ) + fadeIn(
//                        animationSpec = tween(durationMillis = 300)
//                    ),
//                    exit = ExitTransition.None
//                ) {
//                    WordCard(word, navController) { viewModel.toggleBookmark(it) }
//                }
//            }
//        }
//    }
//}

@Composable
fun WordsList(
    modifier: Modifier,
    learnedWords: List<LearnedWord>,
    viewModel: WordViewModel,
    navController: NavController
) {
    val primaryBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF7F5F0),
            Color(0xFFF1E4D2),
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(primaryBackground)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(learnedWords) { word ->
                WordCard(word, navController) { viewModel.toggleBookmark(it) }
            }
        }
    }
}










@Composable
fun WordCard(learnedWord: LearnedWord, navController: NavController, onBookmarkClick: (LearnedWord) -> Unit) {
    val primaryColor = Color(0xFFF7E5BE) // Light beige
    val accentColor = Color(0xFF1F565E) // Dark teal

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateCircle(date = learnedWord.learnedAt)

            Spacer(modifier = Modifier.width(16.dp))

            WordInformation(
                word = learnedWord.word,
                definition = learnedWord.description,
                modifier = Modifier.weight(1f).clickable { navController.navigate(NavigationDestinations.Word.createRoute(learnedWord.id))   }
            )
            Spacer(modifier = Modifier.width(16.dp))

            //Testing UI shit

            Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                // Animate background color
                val backgroundColor by animateColorAsState(
                    targetValue = if (learnedWord.bookmarked) Color(0xFF1F565E) else Color(0xFF1F565E).copy(alpha = 0.1f),
                    label = "BackgroundColorAnimation"
                )

                // Animate icon tint
                val iconTint by animateColorAsState(
                    targetValue = if (learnedWord.bookmarked) Color.White else Color(0xFF1F565E),
                    label = "IconTintAnimation"
                )

                // Pop animation on toggle
                val scale by animateFloatAsState(
                    targetValue = if (learnedWord.bookmarked) 1.1f else 1f, // Scale up a bit when bookmarked
                    animationSpec = tween(durationMillis = 150),
                    label = "IconScaleAnimation"
                )

                IconButton(
                    onClick = {
                        onBookmarkClick(learnedWord)
                    },
                    modifier = Modifier
                        .graphicsLayer(scaleX = scale, scaleY = scale) // Bouncy effect
                        .background(
                            color = backgroundColor,
                            shape = CircleShape
                        )
                        .size(38.dp) // Slightly larger button for better touch
                ) {
                    Icon(
                        imageVector = if (learnedWord.bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (learnedWord.bookmarked) "Remove from bookmarks" else "Add to bookmarks",
                        tint = iconTint,
                        modifier = Modifier.size(18.dp) // Larger, consistent icon
                    )
                }
            }




        }
    }
}

@Composable
fun WordInformation(word: String, definition: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = word,
            fontFamily = Playfair,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = definition,
            fontFamily = FontFamily.Default,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.7f),
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// IDK about word color
@Composable
fun DateCircle(date: LocalDate, modifier: Modifier = Modifier) {
    val accentColor = Color(0xFF1F565E) // Dark teal
    val lightBeige = Color(0xFFF7E5BE) // Light beige
    val lightGreen = Color(0xFF1F565E).copy(alpha = 0.1f) // Light beige


    Surface(
        color = lightGreen,
        shape = CircleShape,
        border = BorderStroke(2.dp, accentColor),
        modifier = modifier.size(54.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = date.month.name.take(3),
                color = accentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = date.dayOfMonth.toString(),
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateCircle() {
    LinguaDailyAppTheme {
        DateCircle(date = LocalDate.now())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordInformation() {
    LinguaDailyAppTheme {
        WordInformation(
            word = "Serendipity",
            definition = "The occurrence and development of events by chance in a happy or beneficial way"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordCard() {
    val navController = rememberNavController()

    LinguaDailyAppTheme {
        WordCard(
            learnedWord = LearnedWord(
                word = "Serendipity",
                description = "The occurrence and development of events by chance in a happy or beneficial way",
                language = Language.ENGLISH,
                phoneticSpelling = "",
                exampleSentence = "",
                partOfSpeech = "",
                etymology = ""
            ),
            navController,
            onBookmarkClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyWordsView() {
    LinguaDailyAppTheme {
        EmptyWordsView()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordsScreen() {
    val navController = rememberNavController()
    LinguaDailyAppTheme  {
        WordsScreen(navController = navController)
    }
}