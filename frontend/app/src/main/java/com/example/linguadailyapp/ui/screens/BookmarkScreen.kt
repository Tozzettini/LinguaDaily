package com.example.linguadailyapp.ui.screens

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GppMaybe
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
import org.intellij.lang.annotations.JdkConstants
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(navController: NavController, viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(
    LocalContext.current)
)
) {
    val bookmarkedWords by viewModel.bookmarkedWords.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bookmarks Bombaclaat") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(NavigationDestinations.Home.route) // Fallback if no previous screen
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
                    }
                }
            )
        },
        content = { paddingValues ->
            BookmarkGrid(Modifier.padding(paddingValues), bookmarkedWords, {word -> viewModel.toggleBookmark(word)})
        }
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewBookmarkScreen() {
    val navController = rememberNavController() // ✅ Correct way to provide a NavController in preview
    MaterialTheme {
        BookmarkScreen(
            navController = navController
        )
    }
}

//----
// BookmarkBoxContainer which contains everything and will be contained in a BookmarkGrid

@Composable
fun BookmarkBoxContainer(word: Word, onTrashClicked : (Word) -> Unit) {

        Box(
            modifier = Modifier
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)) // Black border outline
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) // Background color & rounded corners
                .clip(RoundedCornerShape(8.dp)) // Ensures content inside respects the rounded shape
                .widthIn(min = 10.dp, max = 150.dp) // Minimum width
                .padding(8.dp)
        ) {
            //Background is the Box

            Column() {
                //Container for all text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val formatter = DateTimeFormatter.ofPattern("dd MMM 'at' hh:mm a", Locale.ENGLISH)
                    val formattedDate = word.bookmarkedAt!!.format(formatter)

                    //Timestamp and Icon
                    Text(
                        text = formattedDate,
                        color = Color.Gray, // Set text color to grey
                        fontSize = 10.sp, // Set font size to small
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "trash", // Use text for accessibility
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp).clickable {
                            onTrashClicked(word)
                        }
                        //Onclick eliminates
                    )

                }
                Spacer(
                    modifier = Modifier
                        .padding(0.dp, 5.dp)
                        .fillMaxWidth(0.9f) // 90% of the parent's width (adjust as needed)
                        .height(1.dp) // Thin line
                        .background(Color.Gray) // Black separator
                        .align(Alignment.CenterHorizontally) // Center it inside a Column or Box
                )
                Text(
                    text = word.word,
                    color = Color.Black, // Set text color to grey
                    fontSize = 18.sp, // Set font size to small
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = word.description,
                    color = Color.Black, // Set text color to grey
                    fontSize = 14.sp, // Set font size to small
                    lineHeight = 10.sp
                )
            }

        }
    }


@Preview
@Composable
fun PreviewBookmarkBoxContainer() {
    MaterialTheme{
    BookmarkBoxContainer(Word(word = "Ciao", description = "Hello", language = "Italian", date = LocalDate.now()), onTrashClicked = {})
 }
}

//---


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkGrid(modifier: Modifier = Modifier, words: List<Word>, onTrashClicked: (Word) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2-column grid
        modifier = modifier.fillMaxSize().padding(8.dp,8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Space between rows
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between columns
    ) {
        items(words) {
            word -> BookmarkBoxContainer(word, onTrashClicked)
        }
    }
}


// Idk how to pass a viewmodel here so I just commented it
//@Preview
//@Composable
//fun PreviewBookmarkGrid() {
//    MaterialTheme{
//        BookmarkGrid(words = listOf(Word(word = "Ciao", description = "Hello", language = "Italian", date = LocalDate.now()))
//    }
//}