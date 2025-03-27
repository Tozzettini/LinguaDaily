package com.example.linguadailyapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.time.LocalDate
import java.time.MonthDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(navController: NavController, viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))) {
    val words by viewModel.words.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Words till date") },
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
            WordsList(Modifier.padding(paddingValues), words)
        }
    )

}

@Composable
fun WordsList(modifier: Modifier, words: List<Word>) {

    Surface (modifier = modifier.background(Color.LightGray)) {
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
        {
            items(words) {
                    item -> WordCard(item)
            }
        }
    }
}

@Composable
fun WordCard(word: Word) {

    Row (modifier = Modifier
        .fillMaxWidth()
        .height(66.dp)
        .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateCircle(
            date = word.date,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        WordInformation(word.word, word.description)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.BookmarkBorder,
            contentDescription = "Bookmark",
            tint = Color.DarkGray
        )
        Spacer(modifier = Modifier.width(8.dp))
    }

}

@Composable
fun WordInformation(word: String, definition: String) {

    Column {
        Text(
            text = word,
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = definition,
            color = Color.DarkGray,
            fontSize = 12.sp
        )

    }

}

@Composable
fun DateCircle(date: LocalDate, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape,
        modifier = modifier.size(50.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = Color.Blue,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = 6.dp)
            )
            Text(
                text = date.month.name.take(3),
                color = Color.Blue,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = -6.dp)
            )
        }
    }
}
//TODO: Using offset isn't a Great Idea in theory

//@Preview(showBackground = true)
//@Composable
//fun previewWordsList() {
//    MaterialTheme {
//        WordsList(Modifier)
//    }
//}

@Preview(showBackground = true)
@Composable
fun previewDateCircle() {
    MaterialTheme {
        DateCircle(date = LocalDate.now(), Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun previewWordInformation() {
    MaterialTheme {
        WordInformation("ciao tutti", "buongiorno")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun previewWordCard() {
//    MaterialTheme {
//        WordCard()
//    }
//}

@Preview(showBackground = true)
@Composable
fun previewWordsScreen() {
    val navController = rememberNavController()
    MaterialTheme {
        WordsScreen(
            navController = navController
        )
    }
}


//---
@Composable
fun TestDateCircle(monthDay: MonthDay, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape,
        modifier = modifier.size(50.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                //wrote with $ cause its supposed to be a variable, its called String Interpolation
                // Bomboclaat
                text = "${monthDay.month.name.take(3)}\n${monthDay.dayOfMonth}",
                color = Color.Blue,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp // Adjust spacing between lines
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewTestDateCircle() {
    MaterialTheme {
        TestDateCircle(monthDay = MonthDay.now(), Modifier.padding(8.dp))
    }
}
