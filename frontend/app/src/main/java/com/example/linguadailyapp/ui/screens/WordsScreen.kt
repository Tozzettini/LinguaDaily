package com.example.linguadailyapp.ui.screens

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.components.LinguaBottomNavigation
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.ui.theme.Playfair
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
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
                )
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
                    words = words,
                    viewModel = viewModel
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

@Composable
fun WordsList(modifier: Modifier, words: List<Word>, viewModel: WordViewModel) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(words) { word ->
                WordCard(word) { viewModel.toggleBookmark(it) }
            }
        }
    }
}

@Composable
fun WordCard(word: Word, onBookmarkClick: (Word) -> Unit) {
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
            DateCircle(date = word.date)

            Spacer(modifier = Modifier.width(16.dp))

            WordInformation(
                word = word.word,
                definition = word.description,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = { onBookmarkClick(word) },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (word.bookmarked) accentColor else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(horizontal = 0.dp)
            ) {
                Icon(
                    imageVector = if (word.bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = if (word.bookmarked) "Remove bookmark" else "Add bookmark",
                    tint = if (word.bookmarked) Color.White else accentColor,
                    modifier = Modifier.size(20.dp)
                )
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

@Composable
fun DateCircle(date: LocalDate, modifier: Modifier = Modifier) {
    val accentColor = Color(0xFF1F565E) // Dark teal
    val lightBeige = Color(0xFFF7E5BE) // Light beige

    Surface(
        color = lightBeige,
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
                fontWeight = FontWeight.Medium,
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
    LinguaDailyAppTheme {
        WordCard(
            word = Word(
                word = "Serendipity",
                description = "The occurrence and development of events by chance in a happy or beneficial way",
                language = "English",
                date = LocalDate.now(),
                bookmarked = true
            ),
            onBookmarkClick = {}
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