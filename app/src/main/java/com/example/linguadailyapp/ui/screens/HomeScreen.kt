package com.example.linguadailyapp.ui.screens


import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.ui.components.ButtonBanner
import com.example.linguadailyapp.ui.components.TopBar
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.components.MainWordDisplayContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLanguageIconClick: () -> Unit = {},
    onSettingsIconClick: () -> Unit = { }
) {



    Scaffold(
        topBar = {
            TopBar(
                onLanguageIconClick = {
                    // Show the language pop-up
//                    isLanguagePopupVisible = true
                },
                onSettingsIconClick = {
                    // Navigate to the settings screen
                    navController.navigate(NavigationDestinations.Settings.route)
                }
            )
        },
        content = { paddingValues ->
            // Main content of the HomeScreen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
                    {

                        Spacer(modifier = Modifier.height(16.dp))
                        ButtonBanner(navController = navController)
                        // Add your home screen content here
                        // MainWordContainer
                        MainWordDisplayContainer()
                        // SteakCounter
                        // Spacer with weight 1 to push the ad/design always at the bottom
                        Spacer(modifier = Modifier.weight(1f)) // This will take up all available space

                        // Ad/Design
                        MyBoxWithBottomImage()
                    }
        }
    )
}
// TODO: Might have to redo everything with cards
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController() // ✅ Correct way to provide a NavController in preview
    MaterialTheme {
        HomeScreen(
            navController = navController
        )
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

@Composable
fun CircularPopUp() {
    var isPopupVisible by remember { mutableStateOf(false) }

    // Sizes
    val buttonSize = 60.dp
    val circleSize = 60.dp

    // Button click to show/hide the pop-up
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(buttonSize)
                .clip(CircleShape)
                .background(Color.Blue)
                .clickable { isPopupVisible = !isPopupVisible }
        ) {
            Text("•", color = Color.White, fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Pop-up circles with animation
        Box(modifier = Modifier.fillMaxSize()) {
            this@Column.AnimatedVisibility(visible = isPopupVisible) {
                // Circle 1 - Right of the button
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .offset(x = buttonSize)
                        .clip(CircleShape)
                        .background(Color.Red)
                )

                // Circle 2 - Beneath the button
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .offset(y = buttonSize)
                        .clip(CircleShape)
                        .background(Color.Green)
                )

                // Circle 3 - Between Circle 1 and 2
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .offset(
                            x = buttonSize / 2,
                            y = buttonSize / 2
                        )
                        .clip(CircleShape)
                        .background(Color.Yellow)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularPopUp() {
    MaterialTheme {
        CircularPopUp()
    }
}
