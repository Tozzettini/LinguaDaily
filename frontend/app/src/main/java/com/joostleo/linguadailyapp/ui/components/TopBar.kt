package com.joostleo.linguadailyapp.ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joostleo.linguadailyapp.R
import com.joostleo.linguadailyapp.navigation.NavigationDestinations
import com.joostleo.linguadailyapp.datamodels.Language
import com.joostleo.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.joostleo.linguadailyapp.ui.theme.Playfair
import com.joostleo.linguadailyapp.utils.preferences.LanguagePreferencesManager
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModel

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    LinguaDailyAppTheme {

        ImprovedStyledTopBar(
            navController = rememberNavController(),
            viewModel = LanguageViewModel(
                languagePreferencesManager = LanguagePreferencesManager(
                    context = LocalContext.current
                )
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedStyledTopBar(
    navController: NavController,
    viewModel: LanguageViewModel
) {
    val languages = Language.entries

    // State to keep track of dropdown visibility
    var showLanguageDropdown by remember { mutableStateOf(false) }

    // Collect the selectedLanguages from the ViewModel
    val selectedLanguages by viewModel.selectedLanguages.collectAsState()

    // Local state for dropdown selections - initialized with current selections
    var tempSelectedLanguages by remember(selectedLanguages) {
        mutableStateOf(selectedLanguages.toMutableSet())
    }

    // Brand color consistent with the rest of the app
    val brandColor = Color(0xFF1F565E)
    val backgroundColor = Color(0xFFF7F7F7)

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        ),
        title = {
            // Logo in the center with elegant typography
            Text(
                text = "LinguaDaily",
                fontFamily = Playfair,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = brandColor,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            // Language selector with improved visual design
            Box {
                // Badge showing additional languages when multiple are selected
                AnimatedVisibility(
                    visible = selectedLanguages.size > 1,
                    enter = fadeIn() + scaleIn(),
                ) {
                    Text(
                        text = "+${selectedLanguages.size - 1}",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .offset(x = 32.dp, y = (-4).dp)
                            .size(16.dp)
                            .background(brandColor, CircleShape)
                            .wrapContentSize(Alignment.Center)
                    )
                }

                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = brandColor.copy(alpha = 0.08f)
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                showLanguageDropdown = true
                                tempSelectedLanguages = selectedLanguages.toMutableSet()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Show first selected language flag or a default icon
                        if (selectedLanguages.isNotEmpty()) {
                            Text(
                                text = selectedLanguages.firstOrNull()?.flag ?: "🌐",
                                fontSize = 18.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = brandColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // Language selection dropdown
                DropdownMenu(
                    expanded = showLanguageDropdown,
                    offset = DpOffset(x = 28.dp, y = (-8).dp),
                    onDismissRequest = {
                        showLanguageDropdown = false
                        // Cancel changes if dismissed without clicking Done
                        tempSelectedLanguages = selectedLanguages.toMutableSet()
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(elevation = 0.dp)
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
                                    // Toggle selection in temp state only
                                    val updatedSelection = tempSelectedLanguages.toMutableSet()
                                    if (updatedSelection.contains(language)) {
                                        updatedSelection.remove(language)
                                    } else {
                                        updatedSelection.add(language)
                                    }
                                    tempSelectedLanguages = updatedSelection
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = language.displayName,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Text(
                                text = language.flag,
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )

                            // Show checkmark if selected
                            if (tempSelectedLanguages.contains(language)) {
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

                    // Add a done button to close the dropdown and confirm selections
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE0E0E0)
                    )

                    Button(
                        onClick = {
                            showLanguageDropdown = false
                            // Save to ViewModel which persists to SharedPreferences
                            viewModel.updateSelectedLanguages(tempSelectedLanguages)

                            // TODO: Force reload because of bug where if the selected
                            //  languages are updated is it not seen in the wordViewModel.
                            //  Maybe try to actually fix the bug later
                            navController.navigate(NavigationDestinations.Home.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1F565E)
                        )
                    ) {
                        Text(
                            text = "Done (${tempSelectedLanguages.size})",
                            color = Color.White
                        )
                    }
                }
            }
        },
        actions = {
            // Settings button with improved visual design
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = brandColor.copy(alpha = 0.08f)
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate(NavigationDestinations.Settings.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = brandColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .drawBehind {
                // Draw a thin top border
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height),  // Start at bottom-left
                    end = Offset(size.width, size.height),  // End at bottom-right
                    strokeWidth = 2.dp.toPx()
                )
            },
        scrollBehavior = null
    )


}