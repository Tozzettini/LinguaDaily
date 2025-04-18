package com.example.linguadailyapp.ui.components

import TwoCardsInRow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.database.learnedWord.LearnedWord
import com.example.linguadailyapp.database.learnedWord.WordDetails
import com.example.linguadailyapp.database.learnedWord.sampleWordDetails
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.screens.NavigationIcon
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.ui.theme.Playfair
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory

@Preview(showBackground = true)
@Composable
fun MainWordCardPreview() {
    LinguaDailyAppTheme {
//        Column {

//            TwoCardsInRow()
//            Spacer(modifier = Modifier.height(16.dp))
        val navController = rememberNavController() // This would normally be used inside the NavHost

            MainWordCard(navController = navController)
//        }
    }
}



@Composable
fun MainWordCard(
    navController: NavController,
    learnedWord : LearnedWord = LearnedWord.default(),
    wordViewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = {pageCount})
    var expanded by remember { mutableStateOf(false) }

    var isBookmarked by rememberSaveable { mutableStateOf(learnedWord.bookmarked) }

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .heightIn(min = 208.dp, max = if (expanded) 400.dp else 150.dp)
            .padding(bottom = 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = learnedWord.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Playfair,
                        color = Color.Black
                    )
                    Text(
                        text = "${learnedWord.partOfSpeech} - ${learnedWord.phoneticSpelling}",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp),
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }



            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(600))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate(NavigationDestinations.Word.createRoute(learnedWord.id))   }
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f) // Let the pager fill most of the height
                    ) { page ->
                        when (page) {
                            0 -> {
                                // Page 1: How to use
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "How to use ${learnedWord.word}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )

                                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                                            IconButton(
                                                onClick = {
                                                    wordViewModel.toggleBookmark(learnedWord)
                                                    isBookmarked = !isBookmarked
                                                },
                                                modifier = Modifier
                                                    .background(
                                                        color = Color(0xFF1F565E),
                                                        shape = CircleShape
                                                    )
                                                    .size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkAdd,
                                                    contentDescription = if (isBookmarked) "Remove from bookmarks" else "Add to bookmarks",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = learnedWord.exampleSentence,
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            lineHeight = 24.sp
                                        )
                                    }
                                }
                            }

                            1 -> {
                                // Page 2: Definition
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Definition",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = learnedWord.description,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        lineHeight = 24.sp
                                    )
                                }
                            }

                            2 -> {
                                // Page 3: Etymology
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Etymology",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = learnedWord.etymology,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }

                    // Dots indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(8.dp)
                                    .background(
                                        color = if (pagerState.currentPage == index) Color(0xFF1F565E) else Color(0xFFD9D9D9),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }




//end of Middile section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(onClick = { navController.navigate(
                        NavigationDestinations.WordsList.route) })
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
                    modifier = Modifier.clickable(onClick = { navController.navigate(
                        NavigationDestinations.Bookmark.route) })
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
                    modifier = Modifier.clickable(onClick = { navController.navigate(
                        NavigationDestinations.WordsList.route) })
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

//----------------------------------------------

@Preview(showBackground = true)
@Composable
fun MainWordCardPreview2() {
    LinguaDailyAppTheme {
//        Column {

//            TwoCardsInRow()
//            Spacer(modifier = Modifier.height(16.dp))
        val navController = rememberNavController() // This would normally be used inside the NavHost

        MainWordCard2(navController = navController)
//        }
    }
}

@Composable
fun MainWordCard2(
    navController: NavController,
    learnedWord : LearnedWord = LearnedWord.default(),
    wordViewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = {pageCount})
    var expanded by remember { mutableStateOf(false) }

    var isBookmarked by rememberSaveable {mutableStateOf(learnedWord.bookmarked)}

    val animatedHeight by animateDpAsState(
        targetValue = if (expanded) 400.dp else 105.dp,
        animationSpec = tween(durationMillis = 400)
    )

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .heightIn(min = 208.dp, max = animatedHeight)
            .padding(bottom = 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // This clips the Row's corners
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding( all = 2.dp)) {
                    Text(
                        text = learnedWord.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Playfair,
                        color = Color.Black
                    )
                    Text(
                        text = "${learnedWord.partOfSpeech} - ${learnedWord.phoneticSpelling}",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp),
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }



            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(400)),
                exit = shrinkVertically(animationSpec = tween(400))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate(NavigationDestinations.Word.createRoute(learnedWord.id))   }
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f) // Let the pager fill most of the height
                    ) { page ->
                        when (page) {
                            0 -> {
                                // Page 1: How to use
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "How to use ${learnedWord.word}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )

                                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                                            IconButton(
                                                onClick = {
                                                    wordViewModel.toggleBookmark(learnedWord)
                                                    isBookmarked = !isBookmarked
                                                },
                                                modifier = Modifier
                                                    .background(
                                                        color = Color(0xFF1F565E),
                                                        shape = CircleShape
                                                    )
                                                    .size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkAdd,
                                                    contentDescription = if (isBookmarked) "Remove from bookmarks" else "Add to bookmarks",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = learnedWord.exampleSentence,
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            lineHeight = 24.sp
                                        )
                                    }
                                }
                            }

                            1 -> {
                                // Page 2: Definition
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Definition",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = learnedWord.description,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        lineHeight = 24.sp
                                    )
                                }
                            }

                            2 -> {
                                // Page 3: Etymology
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Etymology",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = learnedWord.etymology,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }

                    // Dots indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(8.dp)
                                    .background(
                                        color = if (pagerState.currentPage == index) Color(0xFF1F565E) else Color(0xFFD9D9D9),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }




//end of Middile section
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.clickable(onClick = { navController.navigate(
//                        NavigationDestinations.WordsList.route) })
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Lightbulb,
//                        contentDescription = "Words",
//                        tint = Color(0xFF1F565E),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "Words",
//                        fontFamily = FontFamily.Default,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF1F565E)
//                    )
//                }
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.clickable(onClick = { navController.navigate(
//                        NavigationDestinations.Bookmark.route) })
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Bookmarks,
//                        contentDescription = "Bookmarks",
//                        tint = Color(0xFF1F565E),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "Bookmarks",
//                        fontFamily = FontFamily.Default,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF1F565E)
//                    )
//                }
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.clickable(onClick = { navController.navigate(
//                        NavigationDestinations.WordsList.route) })
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Shuffle,
//                        contentDescription = "Random",
//                        tint = Color(0xFF1F565E),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "Random",
//                        fontFamily = FontFamily.Default,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF1F565E)
//                    )
//                }
//            }
        }
    }
}