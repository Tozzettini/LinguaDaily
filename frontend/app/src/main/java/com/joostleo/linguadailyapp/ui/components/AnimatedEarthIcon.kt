package com.joostleo.linguadailyapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.joostleo.linguadailyapp.ui.theme.LinguaDailyAppTheme

@Composable
fun AnimatedEarthIcon() {

//    val vector = painterResource(id = R.drawable.your_svg_icon)

    // Create infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "earth-animation")

    // Vertical bounce animation
    val yOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Rotation animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Apply both animations to the Earth icon
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            //replace with a custom svg if needed
            //if offiline use the offline icon?

            imageVector = Icons.Outlined.Public,
            contentDescription = "Earth Icon",
            modifier = Modifier
                .size(80.dp)
//                .rotate(rotation)
                .graphicsLayer {
                    translationY = yOffset
                },
            tint = Color.Black
        )
    }
}

@Preview
@Composable
fun PreviewAnimatedEarthIcon() {
    LinguaDailyAppTheme {
        AnimatedEarthIcon()
    }

}
@Preview
@Composable
fun PreviewAnimatedEarth3DIcon() {
    LinguaDailyAppTheme {
        LottieSpinningGlobe()
    }

}

@Composable
fun LottieSpinningGlobe() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("globe_animation.json"))
    LottieAnimation(
        composition = composition,
        modifier = Modifier.size(80.dp),
        iterations = LottieConstants.IterateForever // This ensures infinite looping
    )}