package com.joostleo.linguadailyapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchSetting(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Text(text = label)
        },
        trailingContent = {
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SwitchSettingPreview() { // Renamed to avoid conflict
    // Use `remember` to manage the state for the preview
    val (isChecked, onCheckedChange) = remember { mutableStateOf(false) }

    SwitchSetting(
        label = "Enable Notifications", // Example label
        isChecked = isChecked, // Boolean state
        onCheckedChange = onCheckedChange // Lambda to update state
    )
}