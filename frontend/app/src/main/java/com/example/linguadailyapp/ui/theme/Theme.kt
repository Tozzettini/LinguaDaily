package com.example.linguadailyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.linguadailyapp.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    //
//    primaryContainer = Color(0xFFF7E5BE),
//    secondaryContainer = Color(0xFFFDF2E0),
//    onSurface = Color(0xFFFCEDD6),
    //
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
)

val customLightColors = lightColorScheme(
    primary = Color(0xFFFDF5E6), // Container main color
    primaryContainer = Color(0xFFFDF5E6), // Container main color
    onPrimaryContainer = Color.Black, // Container Text color
    secondaryContainer = Color(0xFFFDF2E0), // Container secondary color
    onBackground = Color(0xFFF7E5BE), // Stroke/Border color
    surface = Color(0xFFF1E4D2),
    // other color overrides...
)

val Quicksand = FontFamily(
    Font(R.font.quicksand_light, FontWeight.Light),
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_semibold, FontWeight.SemiBold),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)

val Playfair = FontFamily(
    Font(R.font.playfairdisplay_bold, FontWeight.Bold),
    )



@Composable
fun LinguaDailyAppTheme(
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
////            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
////            Always light mode for now
//            if (darkTheme) dynamicLightColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }



    MaterialTheme(
        colorScheme = customLightColors,
        typography = AppTypography,
        content = content
    )


}