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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joostleo.linguadailyapp.R
import com.joostleo.linguadailyapp.navigation.NavigationDestinations
import com.joostleo.linguadailyapp.datamodels.Language
import com.joostleo.linguadailyapp.viewmodel.LanguageViewModel

// Define the Languagetype class if it's not already defined elsewhere
@Composable
fun ImprovedStyledTopBar3(
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

    // Animated colors with smoother transition
    val containerColor = animateColorAsState(
        targetValue = Color.White,
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color(0xFF1F565E).copy(alpha = 0.08f),
                    ambientColor = Color(0xFF1F565E).copy(alpha = 0.04f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor.value),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Language selector with improved visual design
            Box(modifier = Modifier.padding(start = 14.dp)) {

                this@Row.AnimatedVisibility(
                    visible = selectedLanguages.size > 1,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Text(
                        text = "+${selectedLanguages.size - 1}",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .offset(x = 22.dp, y = (-4).dp)
                            .size(16.dp)
                            .background(Color(0xFF1F565E), CircleShape)
                            .wrapContentSize(Alignment.Center)
                    )
                }

                IconButton(
                    onClick = {
                        showLanguageDropdown = true
                        tempSelectedLanguages = selectedLanguages.toMutableSet()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1F565E).copy(alpha = 0.12f),
                                    Color(0xFF1F565E).copy(alpha = 0.06f)
                                )
                            )
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
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
                                tint = Color(0xFF1F565E),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showLanguageDropdown,
                    offset = DpOffset(x = 28.dp, y = (-8).dp),
                    onDismissRequest = {
                        showLanguageDropdown = false
                        tempSelectedLanguages = selectedLanguages.toMutableSet()
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color(0xFF1F565E).copy(alpha = 0.1f)
                        )
                        .width(200.dp)
                ) {
                    Text(
                        text = "Select Languages",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F565E),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
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
                                    val updatedSelection = tempSelectedLanguages.toMutableSet()
                                    if (updatedSelection.contains(language)) {
                                        updatedSelection.remove(language)
                                    } else {
                                        updatedSelection.add(language)
                                    }
                                    tempSelectedLanguages = updatedSelection
                                }
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = language.flag,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Text(
                                text = language.name,
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )

                            // Show checkmark if selected with animation
                            AnimatedVisibility(
                                visible = tempSelectedLanguages.contains(language),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF1F565E),
                                                    Color(0xFF1F565E).copy(alpha = 0.9f)
                                                )
                                            ),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            if (!tempSelectedLanguages.contains(language)) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .border(1.dp, Color(0xFF1F565E), CircleShape)
                                )
                            }
                        }

                        if (language != languages.last()) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE0E0E0)
                            )
                        }
                    }

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
                            viewModel.updateSelectedLanguages(tempSelectedLanguages)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1F565E)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Done (${tempSelectedLanguages.size})",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Logo in the center with improved scaling
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_no_bg),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(45.dp)
                        .padding(top = 6.dp)
                        .graphicsLayer(
                            scaleX = 1.8f,
                            scaleY = 1.8f
                        ),
                    colorFilter = ColorFilter.tint(Color(0xFF1F565E))
                )
            }

            // Settings button with improved visual design
            Box(modifier = Modifier.padding(end = 14.dp)) {
                IconButton(
                    onClick = { navController.navigate(NavigationDestinations.Settings.route) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1F565E).copy(alpha = 0.12f),
                                    Color(0xFF1F565E).copy(alpha = 0.06f)
                                )
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF1F565E),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ImprovedStyledTopBarPreview() {
//    LinguaDailyAppTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(480.dp)
//                .background(Color.White)
//        ) {
//            ImprovedStyledTopBar3(
//                navController = rememberNavController(),
//                viewModel = LanguageViewModel(preferredLanguage = "it")
//            )
//        }
//    }
//}