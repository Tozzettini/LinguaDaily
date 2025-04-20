package com.example.linguadailyapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.ui.theme.Playfair
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    navController: NavController,
    wordId : Int? = null,
    wordViewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val scrollState = rememberScrollState()
    var contentVisible by remember { mutableStateOf(false) }

    var learnedWord : LearnedWord
    runBlocking {
        learnedWord = wordViewModel.getLearnedWordById(wordId!!)!!
    }

    var isBookmarked by rememberSaveable { mutableStateOf(learnedWord.bookmarked) }

    LaunchedEffect(Unit) {
        delay(300)
        contentVisible = true
    }
    val primaryBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF7F5F0),  // Light cream color
            Color(0xFFF1E4D2),  // Slightly darker cream
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Word Details", fontFamily = Playfair) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* share logic */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = {
                        wordViewModel.toggleBookmark(learnedWord)
                        isBookmarked = !isBookmarked
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut()
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .background(brush = primaryBackground)
                        .padding(20.dp)
                ) {
                    // Word + Pronunciation
                    Text(
                        text = learnedWord.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Playfair,
                        color = Color.Black,

                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                            .padding(bottom = 10.dp)) {
                        Text(
                            text = "${learnedWord.partOfSpeech} • ${learnedWord.phoneticSpelling}",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(8.dp))
//                        Icon(
//                            imageVector = Icons.Default.VolumeUp,
//                            contentDescription = "Play audio",
//                            tint = Color(0xFF00796B),
//                            modifier = Modifier
//                                .size(20.dp)
//                                .clickable { /* play audio */ }
//                        )
                    }

//                    Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)

                    // Sections
                    DetailSection("How to use", learnedWord.exampleSentence)
                    DetailSection("Definition", learnedWord.description)
                    DetailSection("Etymology", learnedWord.etymology)
//                    DetailSection("Example Sentences", wordDetails.examples.joinToString("\n\n") { "- $it" })

//                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}


@Composable
fun DetailSection(title: String, content: String) {
    SectionDivider()
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Default,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 12.dp)
    )
    Text(
        text = content,
        fontSize = 16.sp,
        color = Color.Black,
        lineHeight = 24.sp
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NavigationIcon(Icons.Default.Lightbulb, "Words") {
            navController.navigate(NavigationDestinations.WordsList.route)
        }
        NavigationIcon(Icons.Default.Bookmarks, "Bookmarks") {
            navController.navigate(NavigationDestinations.Bookmark.route)
        }
        NavigationIcon(Icons.Default.Shuffle, "Random") {
            navController.navigate(NavigationDestinations.WordsList.route)
        }
    }
}

@Composable
fun NavigationIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF1F565E),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontFamily = FontFamily.Default,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F565E)
        )
    }
}

@Composable
fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xFFF7E5BE))
    )
}

@Preview(showBackground = true)
@Composable
fun WordDetailScreenPreview() {
    LinguaDailyAppTheme {
        val navController = rememberNavController()
        WordDetailScreen(navController = navController)
    }
}
