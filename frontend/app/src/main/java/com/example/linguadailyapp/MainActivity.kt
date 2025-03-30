package com.example.linguadailyapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.linguadailyapp.database.settings.SettingsRepository
import com.example.linguadailyapp.database.word.WordRepository
import com.example.linguadailyapp.navigation.AppNavigation
import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
import com.example.linguadailyapp.utils.NotificationPermission
import com.example.linguadailyapp.viewmodel.SyncViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        NotificationPermission(
            this,
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted ->
                NotificationPermission.handlePermissionResult(this, isGranted)
            }
        ).launch()

        //Starts with false since system inital setting is Lightmode
        var isDarkmode = getDarkModeSetting(this)

        try {
            var syncViewModel = SyncViewModel(
                WordRepository(this@MainActivity),
                SettingsRepository(this@MainActivity)
            )
            GlobalScope.launch {
                syncViewModel.sync()
            }
        } catch (e: Exception) {
            // We try again later...
        }

        setContent {
            LinguaDailyAppTheme(darkTheme = isDarkmode) {
                AppNavigation().start()
            }
        }
    }
}


fun saveDarkModeSetting(context: Context, isDarkMode: Boolean) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    with(sharedPreferences.edit()) {
        putBoolean("dark_mode", isDarkMode)

        apply()
    }

}

fun getDarkModeSetting(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("dark_mode", false) // Default to light mode
}
