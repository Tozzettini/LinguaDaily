@file:Suppress("UNREACHABLE_CODE")

package com.example.linguadailyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linguadailyapp.R
import com.example.linguadailyapp.navigation.NavigationDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onLanguageIconClick: () -> Unit = {},
    onSettingsIconClick: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var language by rememberSaveable  { mutableStateOf("EN") }
    var expanded by remember { mutableStateOf(false) }


    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo_no_bg), // your logo image
                contentDescription = null, // provide a description if needed
                modifier = Modifier
                    .padding(vertical = 0.dp) // Optional padding
                    .graphicsLayer(translationY = 20f)
                    .graphicsLayer(scaleX = 3.0f, scaleY = 3.0f)
            )

        },
        navigationIcon = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start, // No space between elements
                modifier = Modifier.padding(0.dp).clip(RoundedCornerShape(16.dp)).clickable(onClick = {


                }),
            ) {
                // Language Icon

                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Language Icon",
                        modifier = Modifier.padding(10.dp)
                    )



                // "EN" text
                Row  ( verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = (-8).dp)

                ) {
                    Text(
                        text = language,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black
                        ),
                        fontWeight = FontWeight.Bold,
                    )

                    // Downward Arrow Icon for dropdown

                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )

                }
            }
//
        },
        actions = {
            IconButton(onClick = onSettingsIconClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    MaterialTheme {
        TopBar(
            onLanguageIconClick = {
                // Handle back navigation in the preview
            },
            onSettingsIconClick = {
                // Handle menu icon click in the preview
            }
        )
    }
}

@Composable
fun LanguageDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    var language by rememberSaveable  { mutableStateOf("EN") }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // No space between elements
            modifier = Modifier.padding(0.dp).clip(RoundedCornerShape(16.dp)).clickable(onClick = {
                expanded = !expanded
            }),
        ) {
            // Language Icon

            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = "Language Icon",
                modifier = Modifier.padding(10.dp)
            )



            // "EN" text
            Row  ( verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(x = (-8).dp)

            ) {
                Text(
                    text = language,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black
                    ),
                    fontWeight = FontWeight.Bold,
                )

                // Downward Arrow Icon for dropdown

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown"
                )

            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // First section
            DropdownMenuItem(
                text = { Text("Profile") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Second section
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                leadingIcon = { Icon(Icons.Outlined.Feedback, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Third section
            DropdownMenuItem(
                text = { Text("About") },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Help") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDropdownMenuPreview() {
    var expanded by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("EN") }

    LanguageDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        onLanguageSelected = { lang ->
            language = lang
            expanded = false // Close dropdown when a language is selected
        }
    )
}


