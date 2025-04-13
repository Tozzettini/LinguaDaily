package com.example.linguadailyapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.database.word.WordDetails
import com.example.linguadailyapp.database.word.sampleWordDetails
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.ui.theme.Playfair

@Composable
fun WordDetailScreen(
    navController: NavController,
    wordDetails: WordDetails = sampleWordDetails // Default to sample data if not provided
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7E5BE))
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .fillMaxHeight()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
                    .verticalScroll(scrollState)
            ) {
                // Word header with back button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        IconButton(
                            onClick = { /* Add bookmark functionality */ },
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF1F565E),
                                    shape = CircleShape
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector =
                                    if (wordDetails.isBookmarked) Icons.Default.Bookmark
                                    else Icons.Default.BookmarkAdd,
                                contentDescription =
                                    if (wordDetails.isBookmarked) "Remove from bookmarks"
                                    else "Add to bookmarks",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Word and part of speech
                Text(
                    text = wordDetails.word,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Playfair,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "${wordDetails.partOfSpeech} - ${wordDetails.pronunciation}",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.SemiBold,
                )

                // Section 1: How to use
                SectionDivider()

                Text(
                    text = "How to use ${wordDetails.word}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Text(
                    text = wordDetails.usageSamples.joinToString(separator = "\n\n"),
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Section 2: Definition
                SectionDivider()

                Text(
                    text = "Definition",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Text(
                    text = wordDetails.definition,
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Section 3: Etymology
                SectionDivider()

                Text(
                    text = "Etymology",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Text(
                    text = wordDetails.etymology,
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Section 4: Example sentences
                SectionDivider()

                Text(
                    text = "Example Sentences",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Text(
                    text = wordDetails.examples.mapIndexed { index, example ->
                        "${index + 1}. $example"
                    }.joinToString(separator = "\n\n"),
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Navigation row at the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = { navController.navigate(NavigationDestinations.WordsList.route) })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Words",
                            tint = Color(0xFF1F565E),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Words",
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F565E)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = { navController.navigate(NavigationDestinations.Bookmark.route) })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = "Bookmarks",
                            tint = Color(0xFF1F565E),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Bookmarks",
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F565E)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = { navController.navigate(NavigationDestinations.WordsList.route) })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Random",
                            tint = Color(0xFF1F565E),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Random",
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F565E)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordDetailScreenPreview() {
    LinguaDailyAppTheme {
        val navController = rememberNavController()
        WordDetailScreen(navController = navController)
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

