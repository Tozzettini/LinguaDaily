//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.LocalFireDepartment
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.compose.rememberNavController
//import com.example.linguadailyapp.utils.preferences.StreakCounter
//import com.example.linguadailyapp.ui.components.AnimatedEarthIcon
//import com.example.linguadailyapp.ui.theme.LinguaDailyAppTheme
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//
//// TODO: Check height of the cards + padding
//
//@Composable
//fun TwoCardsInRow() {
//
////    Surface(
////        modifier = Modifier.fillMaxSize(),
////        color = MaterialTheme.colorScheme.surface,
////    ) {
//
//        val greeting = remember { getTimeBasedGreeting() }
//        val currentDate = remember { getCurrentDateFormatted() }
//        // In your composable
//        // In your composable
//        val context = LocalContext.current
//
//    var streakCount by rememberSaveable {
//        mutableIntStateOf(StreakCounter.getCurrentStreak(context))
//    }        //
//        val isOnline by  rememberSaveable  { mutableStateOf(true) }
//        //
//        val randomNumberOnlineString = remember {
//            val randomNumber = (4..500).random()
//            "%,d".format(randomNumber)
//        }
//
//    // Update streak when the screen becomes visible
//    LaunchedEffect(Unit) {
//        withContext(Dispatchers.IO) {
//            // This updates the streak based on the current date and returns the new value
//            val updatedStreak = StreakCounter.updateStreak(context)
//            streakCount = updatedStreak
//        }
//    }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding( horizontal = 24.dp, vertical = 10.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
////            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // First Card
//            Card(
//                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
//                modifier = Modifier
//                    .weight(1f)
//                    .height(140.dp)
//                .heightIn(min = 140.dp),
//
//            elevation = CardDefaults.cardElevation(
//                    defaultElevation = 16.dp
//                ),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                ),
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize().widthIn(min = 140.dp),
//                ) {
//                    // Top half
//                    Column(
//                        modifier = Modifier
//                            .weight(1.1f)
//                            .fillMaxWidth()
//                            .widthIn( 100.dp)
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = greeting,
//                            color = MaterialTheme.colorScheme.onPrimaryContainer,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                        )
////                        Spacer(modifier = Modifier.height(4.dp))
//                        //Already pushed max to the button due to the padding
//                        Text(
//                            text = currentDate,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = MaterialTheme.colorScheme.onPrimaryContainer,
//                            modifier = Modifier.padding(top = 0.dp)
////                            lineHeight = 6.sp
//                        )
////                        Spacer(modifier = Modifier.height(24.dp))
//
//                    }
//
//                    // Bottom half with different styling
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                            .background(MaterialTheme.colorScheme.secondaryContainer)
//                            .drawBehind {
//                                val strokeWidth = 2.dp.toPx()
//                                drawLine(
//                                    color = Color(0xFFF7E5BE),
//                                    start = Offset(0f, 0f),
//                                    end = Offset(size.width, 0f),
//                                    strokeWidth = strokeWidth
//                                )
//                            }
//                            .padding(vertical = 12.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.LocalFireDepartment,
//                                contentDescription = "Streak icon",
//                                tint = Color.Black,
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "$streakCount - day streak",
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = MaterialTheme.colorScheme.onSecondaryContainer
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Space between cards
//            Spacer(modifier = Modifier.width(10.dp))
//
//            // Second Card
//            Card(
//                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
//                modifier = Modifier
//                    .weight(1f)
//                    .height(140.dp),
//                elevation = CardDefaults.cardElevation(
//                    defaultElevation = 2.dp
//                ),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                )
//
//
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    AnimatedEarthIcon()
////                    LottieSpinningGlobe()
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ){
//                        Box(
//                            modifier = Modifier
//                                .size(12.dp)
//                                .background(
//                                    color = if (isOnline) Color(0xFFA5E492) else Color.Yellow,
//                                    shape = CircleShape
//
//                                )
//                                // Using alpha 0.7f to make the border color slightly transparent,
//                                // so the background color of the Box can be seen through the border.
//                                // This gives a nice effect of the border blending with the background,
//                                // making the whole thing look more visually appealing.
//                                //
//                                .border(
//                                    width = 1.5.dp,
//                                    color = if (isOnline) Color(0xFF62AF7C).copy(alpha = 0.7f) else Color.Yellow.copy(alpha = 0.7f),
//                                    shape = CircleShape
//                                )
//                        )
//
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = if (isOnline) "$randomNumberOnlineString Online" else "You are offline",
//                            color = MaterialTheme.colorScheme.onPrimaryContainer,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 14.sp
//                        )
//                    }
//
//                }
//            }
//        }
//    }
////}
//
//@Preview(showBackground = true)
//@Preview(name = "Small phone", device = "spec:width=481dp,height=291dp" + "")
//@Composable
//fun TwoCardsPreview() {
//    LinguaDailyAppTheme {
//
//        Column {
//
//        TwoCardsInRow()
//            val navController = rememberNavController() // This would normally be used inside the NavHost
//            MainWordCard(navController = navController)        }
//
//    }
//}
//
//
///**
// * Returns a greeting based on the current time:
// * - 5:00 AM to 11:59 AM: Good Morning
// * - 12:00 PM to 4:59 PM: Good Afternoon
// * - 5:00 PM to 9:59 PM: Good Evening
// * - 10:00 PM to 4:59 AM: Wow you're up late!
// */
//fun getTimeBasedGreeting(): String {
//    val currentHour = LocalTime.now().hour
//
//    return when (currentHour) {
//        in 5..11 -> "Good Morning!"
//        in 12..16 -> "Good Afternoon!"
//        in 17..21 -> "Good Evening!"
//        else -> "Wow you're up late!"
//    }
//}
//
///**
// * Returns the current time formatted as HH:mm
// */
//fun getCurrentTimeFormatted(): String {
//    val formatter = DateTimeFormatter.ofPattern("HH:mm")
//    return LocalTime.now().format(formatter)
//}
///**
// * Returns the current date formatted as Month Day, Year (e.g., April 6, 2025)
// */
//fun getCurrentDateFormatted(): String {
//    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
//    return LocalDate.now().format(formatter)
//}