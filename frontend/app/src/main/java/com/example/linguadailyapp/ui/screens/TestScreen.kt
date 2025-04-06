import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.linguadailyapp.R
import com.example.linguadailyapp.ui.theme.Quicksand

@Composable
fun BoxMorning1(modifier: Modifier = Modifier) {
    MaterialTheme  {
        Box(
            modifier = modifier
                .requiredWidth(width = 175.dp)
                .requiredHeight(height = 148.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 175.dp)
                    .requiredHeight(height = 148.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color(0xfffdf5e6))
                    .border(border = BorderStroke(1.dp, Color(0xfff7e5be)),
                        shape = RoundedCornerShape(15.dp)))
            Text(
                text = "Good morning!",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.01.em,

                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Quicksand),

                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 25.dp,
                        y = 21.dp)
                    .requiredWidth(width = 124.dp)
                    .requiredHeight(height = 24.dp))
            Text(
                text = "April 24, 2024",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.16.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Quicksand),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 40.dp,
                        y = 45.dp)
                    .requiredWidth(width = 94.dp)
                    .requiredHeight(height = 16.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 80.dp)
                    .requiredWidth(width = 175.dp)
                    .requiredHeight(height = 68.dp)
                    .clip(shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    .background(color = Color(0xfffdf2e0)))
            Image(
                painter = painterResource(id = R.drawable.logo_no_bg),
                contentDescription = "Campfire",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 24.dp,
                        y = 98.dp)
                    .requiredSize(size = 30.dp))
            Text(
                text = "3 - day streak",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.25.em,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 52.dp,
                        y = 106.dp)
                    .requiredWidth(width = 94.dp)
                    .requiredHeight(height = 16.dp))
        }
    }
}

@Preview(widthDp = 175, heightDp = 148)
@Composable
private fun BoxMorning1Preview() {
    BoxMorning1(Modifier)
}

