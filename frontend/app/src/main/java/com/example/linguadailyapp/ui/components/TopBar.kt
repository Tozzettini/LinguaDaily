package com.example.linguadailyapp.ui.screens


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme

// Define the Languagetype class if it's not already defined elsewhere
data class Languagetype(val name: String, val code: String)
@Composable
fun ImprovedStyledTopBar(
    navController: NavController,
    onLanguagesSelected: (Set<Languagetype>) -> Unit
) {
    // Available languages
    val languages = listOf(
        Languagetype(name = "English", code = "en"),
        Languagetype(name = "Italian", code = "it"),
        Languagetype(name = "Dutch", code = "nl")
    )

    val languageToFlag = mapOf(
        "en" to "🇺🇸",  // English - United States
        "es" to "🇪🇸",  // Spanish - Spain
        "it" to "🇮🇹",  // Italian - Italy
        "pt" to "🇵🇹",  // Portuguese - Portugal
        "nl" to "🇳🇱",  // Dutch - Netherlands
    )

    // State to keep track of dropdown visibility
    var showLanguageDropdown by remember { mutableStateOf(true) }

    // State to keep track of selected languages - use mutableStateOf with a mutableSetOf
    var selectedLanguages by remember { mutableStateOf(mutableSetOf<Languagetype>()) }

    // Animated colors
    val containerColor = animateColorAsState(
        targetValue = Color.White,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color(0xFF1F565E).copy(alpha = 0.1f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor.value),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Language selector
            Box(modifier = Modifier.padding(start = 12.dp)) {
                IconButton(
                    onClick = { showLanguageDropdown = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1F565E).copy(alpha = 0.08f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        // Show first selected language flag or a default icon
                        if (selectedLanguages.isNotEmpty()) {
                            Text(
                                text = selectedLanguages.firstOrNull()?.let { languageToFlag[it.code] } ?: "🌐",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 2.dp)
                            )

                        } else {
                            // Default icon when no languages selected
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = Color(0xFF1F565E),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                // If more than one language is selected, show a count
                //showing this like a colomn when it should be a row
                if (selectedLanguages.size > 1) {
                    Text(
                        text = "+${selectedLanguages.size - 1}",
                        fontSize = 10.sp,
                        color = Color(0xFF1F565E),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                DropdownMenu(
                    expanded = showLanguageDropdown,
                    onDismissRequest = { showLanguageDropdown = false },
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(elevation = 4.dp)
                        .width(180.dp)
                ) {
                    Text(
                        text = "Select Languages",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F565E),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE0E0E0)
                    )

                    languages.forEach { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Create a new set to trigger recomposition
                                    val updatedSelection = selectedLanguages.toMutableSet()
                                    if (updatedSelection.contains(language)) {
                                        updatedSelection.remove(language)
                                    } else {
                                        updatedSelection.add(language)
                                    }
                                    selectedLanguages = updatedSelection
                                    // Call the callback with the updated set
                                    onLanguagesSelected(updatedSelection)
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = languageToFlag[language.code] ?: "🌐",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Text(
                                text = language.name,
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )

                            // Show checkmark if selected
                            if (selectedLanguages.contains(language)) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color(0xFF1F565E), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .border(1.dp, Color(0xFF1F565E), CircleShape)
                                )
                            }
                        }

                        if (language != languages.last()) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE0E0E0)
                            )
                        }
                    }

//                    // Add a done button to close the dropdown and confirm selections
//                    HorizontalDivider(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 8.dp),
//                        thickness = 1.dp,
//                        color = Color(0xFFE0E0E0)
//                    )
//
//                    Button(
//                        onClick = {
//                            showLanguageDropdown = false
//                            // Final callback to ensure parent component gets the selections
//                            onLanguagesSelected(selectedLanguages)
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF1F565E)
//                        )
//                    ) {
//                        Text(
//                            text = "Done (${selectedLanguages.size})",
//                            color = Color.White
//                        )
//                    }
                }
            }


                    // Logo in the center
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_no_bg),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 12.dp)
                        .graphicsLayer(
                            scaleX = 4.0f,
                            scaleY = 4.0f
                        )
                )
            }

            // Settings button
            Box(modifier = Modifier.padding(end = 12.dp)) {
                IconButton(
                    onClick = { navController.navigate(NavigationDestinations.Settings.route) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1F565E).copy(alpha = 0.08f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF1F565E),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImprovedStyledTopBarPreview() {
    LinguaDailyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(Color.White)
        ) {
            ImprovedStyledTopBar(
                navController = rememberNavController(),
                onLanguagesSelected = {})
        }
    }
}