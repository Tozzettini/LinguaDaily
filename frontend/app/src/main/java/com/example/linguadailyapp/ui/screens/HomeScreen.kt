package com.example.linguadailyapp.ui.screens


import TwoCardsInRow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.database.word.Word
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.ui.components.ButtonBanner
import com.example.linguadailyapp.ui.components.TopBar
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.components.MainWordCard
import com.example.linguadailyapp.ui.components.MainWordDisplayContainer
import com.example.linguadailyapp.ui.components.StyledTopBar
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.viewmodel.WordViewModel
import com.example.linguadailyapp.viewmodel.WordViewModelFactory
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WordViewModel = viewModel(factory = WordViewModelFactory(LocalContext.current))
) {

    val todaysWord by viewModel.todaysWord.collectAsState()

//    Scaffold(
//        topBar = {
//            TopBar(
//                onLanguageIconClick = {
//                    // Show the language pop-up
////                    isLanguagePopupVisible = true
//                },
//                onSettingsIconClick = {
//                    // Navigate to the settings screen
//                    navController.navigate(NavigationDestinations.Settings.route)
//                }
//            )
//        },
//        content = { paddingValues ->
            // Main content of the HomeScreen

    // Define the original light color
    val lightColor = Color(0xFFF1E4D2)

    // Calculate 1% darker color manually
    val darkerColor = lightColor.copy(
        red = lightColor.red * 0.99f,
        green = lightColor.green * 0.99f,
        blue = lightColor.blue * 0.99f
    )

    // Define the gradient
//    val gradient = Brush.linearGradient(
//        colors = listOf(
//            lightColor, // Light color at the top
//            darkerColor // Darker color at the bottom (1% darker)
//        ),
//        start = Offset(0f, 0f),  // Top
//        end = Offset(0f, 1f)     // Bottom
//    )



    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightColor) // Apply background color
                    .padding(top = 10.dp) // Add padding to the bottom
            ) {
                StyledTopBar(navController = navController)
            }        },
        content = { paddingValues ->
            // Main content of the HomeScreen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)  // This ensures correct padding is applied
                    .background(Brush.linearGradient(
                        colors = listOf(lightColor, darkerColor),
                        start = Offset(0f, 0f),
                        end = Offset(0f, 1f)
                    )),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Spacer(modifier = Modifier.height(0.dp))
                // Add your home screen content here
                TwoCardsInRow()
                MainWordCard(navController = navController)

                // This is where my screen ends!
//                Spacer(modifier = Modifier.height(45.dp))
//                Box(modifier = Modifier
//                    .fillMaxWidth()  // Ensure it stretches across the full width
//                    .height(1.dp)   // Set the height to 12.dp
//                    .background(Color.Cyan) // Set the background color to black
//                )
            }

        }
    )

}


// TODO: Might have to redo everything with cards
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController() // ✅ Correct way to provide a NavController in preview
    LinguaDailyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(750.dp) // Set a fixed height for the preview
        ) {
            HomeScreen(navController = navController)
        }
    }

}

@Composable
fun MyBoxWithBottomImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Other content here (e.g., text, buttons)

        Image(
            painter = painterResource(id = R.drawable.bottom_design2),
            contentDescription = "Bottom Design",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth() // Ensures the image spans the width
        )
    }
}
