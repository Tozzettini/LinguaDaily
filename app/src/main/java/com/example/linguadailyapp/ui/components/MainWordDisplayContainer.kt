package com.example.linguadailyapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linguadailyapp.database.word.Word
import java.time.LocalDate

@Composable
fun MainWordDisplayContainer(word: Word) {

    val mainWord = "Bomboclaat"
    val definition = "An exclamation used to express shock or frustration."
    val usageExample = "Bomboclaat! I forgot my keys at home."


    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth() // Takes up most of the width
            .padding(16.dp) // Keeps padding from screen edges
            .wrapContentHeight() // Height adjusts dynamically
            .verticalScroll(rememberScrollState()) // Allows scrolling if content overflows
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                word.word,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(": ${word.description}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text( "How to use $mainWord", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(usageExample, fontSize = 18.sp)
            // Add more content as needed
        }
    }
}
//Contains the Main word
//Defenition
//"How to use + ${MainWord}"
//How to use the main word

@Preview
@Composable
fun PreviewMainWordDisplayContainer(){
MaterialTheme {
    MainWordDisplayContainer(Word(word = "Ciao", description = "Hello", language = "Italian", date = LocalDate.now()))
}
}