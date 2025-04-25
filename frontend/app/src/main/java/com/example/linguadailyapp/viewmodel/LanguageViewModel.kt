package com.example.linguadailyapp.viewmodel

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.linguadailyapp.R
import com.example.linguadailyapp.navigation.NavigationDestinations
import com.example.linguadailyapp.datamodels.Language
import com.example.linguadailyapp.utils.preferences.LanguagePreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel using SharedPreferences for language preferences
class LanguageViewModel(private val languagePreferencesManager: LanguagePreferencesManager) : ViewModel() {
    // MutableStateFlow to hold the current selections
    private val _selectedLanguages = MutableStateFlow<Set<Language>>(setOf())
    val selectedLanguages: StateFlow<Set<Language>> = _selectedLanguages

    init {
        // Load saved languages on initialization
        loadSelectedLanguages()
    }

    private fun loadSelectedLanguages() {
        var languages = languagePreferencesManager.getLanguages()

        if(languages.isEmpty()) languages = setOf(Language.ENGLISH)

        _selectedLanguages.value = languages
    }

    // Function to update selected languages
    fun updateSelectedLanguages(languages: Set<Language>) {
        var languagesToSave = languages

        if(languagesToSave.isEmpty()) return

        languagePreferencesManager.setLanguages(languagesToSave)

        _selectedLanguages.value = languagesToSave
    }
}

// Factory for ViewModel instantiation
class LanguageViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val languagePreferencesManager = LanguagePreferencesManager(context)
            return LanguageViewModel(languagePreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Improved TopBar component using ViewModel with SharedPreferences
@Composable
fun ImprovedStyledTopBar2(
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
                    onClick = {
                        showLanguageDropdown = true
                        // Initialize temp selection with current selection
                        tempSelectedLanguages = selectedLanguages.toMutableSet()
                    },
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
                                text = selectedLanguages.firstOrNull()?.flag ?: "🌐",
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
                if (selectedLanguages.size > 1) {
                    Text(
                        text = "+${selectedLanguages.size - 1}",
                        fontSize = 12.sp,
                        color = Color(0xFF1F565E),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
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

            // Add any other TopBar elements here if needed...
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

// Example usage in your Activity or Fragment
@Composable
fun YourAppScreen() {
    // Get the application context
    val context = LocalContext.current

    // Create the ViewModel using the factory
    val viewModel: LanguageViewModel = viewModel(
        factory = LanguageViewModelFactory(context)
    )

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            ImprovedStyledTopBar2(
                navController = navController,
                viewModel = viewModel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Example of accessing selected languages in your UI
            val selectedLanguages by viewModel.selectedLanguages.collectAsState()

            Text(
                text = "Currently Selected Languages:",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            selectedLanguages.forEach { language ->
                Text(
                    text = "• ${language.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Rest of your app content
        }
    }
}