package com.joostleo.linguadailyapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joostleo.linguadailyapp.ui.theme.Playfair




@Composable
fun FeedbackBox(
    onSubmitFeedback: (String) -> Unit
) {
    val accentColor = Color(0xFF1F565E)
    val lightBeige = Color(0xFFF7E5BE)

    var feedbackText by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Reduced padding for smaller screens
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp) // Slightly smaller icon box
                        .background(
                            color = lightBeige,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Feedback,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp)) // Reduced spacing

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Send Feedback",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Text(
                        text = "Help us improve with your suggestions",
                        fontSize = 13.sp, // Slightly smaller text
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(40.dp) // Smaller icon button
                ) {
                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = accentColor
                    )
                }
            }

            // Expanded feedback section with improved scrollability
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                // Using heightIn to ensure the content doesn't grow too large
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp) // Reduced padding
                        .heightIn(max = 200.dp) // Maximum height constraint
                ) {
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp, max = 120.dp), // Flexible height with constraints
                        placeholder = { Text("Share your experience or report issues...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Reduced spacing

                    // Improved button layout for small screens
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                feedbackText = ""
                                isExpanded = false
                            },
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Gray
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // More compact padding
                            modifier = Modifier.heightIn(min = 36.dp, max = 40.dp) // Flexible height
                        ) {
                            Text("Cancel", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (feedbackText.isNotBlank()) {
                                    onSubmitFeedback(feedbackText)
                                    feedbackText = ""
                                    showConfirmation = true
                                    isExpanded = false
                                }
                            },
                            enabled = feedbackText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor,
                                contentColor = Color.White,
                                disabledContainerColor = accentColor.copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // More compact padding
                            modifier = Modifier.heightIn(min = 36.dp, max = 40.dp) // Flexible height
                        ) {
                            Text("Submit", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            containerColor = Color.White,
            titleContentColor = accentColor,
            textContentColor = Color.Black,
            title = {
                Text(
                    "Thank You!",
                    fontFamily = Playfair,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Your feedback has been submitted successfully. We appreciate your input to help improve the app.",
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showConfirmation = false }
                ) {
                    Text(
                        "OK",
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

// Example implementation in a screen with proper scrollability
@Composable
fun SettingsScreenWithFeedback(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Add scrollability to the entire screen
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ... Other settings items

        FeedbackBox(
            onSubmitFeedback = { feedback ->
                // Handle feedback submission
            }
        )

        // Add extra space at the bottom to ensure everything is accessible
        Spacer(modifier = Modifier.height(16.dp))
    }
}
// Example implementation in a screen with proper scrollability
