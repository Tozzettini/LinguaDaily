package com.example.linguadailyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onLanguageIconClick: () -> Unit = {},
    onSettingsIconClick: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Lingua Daily",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        },
        navigationIcon = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // No space between elements
            ) {
                // Language Icon
                IconButton(
                    onClick = onLanguageIconClick,
                    modifier = Modifier.padding(0.dp) // Remove padding around the icon button
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Language Icon"
                    )
                }

                // "EN" text
                Row  ( verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = (-8).dp)
                ) {
                    Text(
                        text = "EN",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        fontWeight = FontWeight.Bold,
                    )

                    // Downward Arrow Icon for dropdown
                    IconButton(modifier = Modifier.offset(x = (-14).dp),
                        onClick = { /* Handle dropdown click */ },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }
                }
            }
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