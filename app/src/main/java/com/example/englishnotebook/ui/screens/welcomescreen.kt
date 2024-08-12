package com.example.englishnotebook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.englishnotebook.R
import com.example.englishnotebook.ui.theme.LightBlue
import com.example.englishnotebook.ui.theme.LightPurple
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.PastelYellow
import com.example.englishnotebook.ui.theme.PastelPink
import com.example.englishnotebook.ui.theme.Orange
import com.example.englishnotebook.ui.theme.Purple

@Composable
fun WelcomeScreen(navController: NavController) {

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels.toFloat()
    val screenHeight = displayMetrics.heightPixels.toFloat()

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            LightPurple,
            PastelPink,
            PastelYellow,
            PastelPink,
            LightBlue
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, screenHeight),
        tileMode = TileMode.Clamp
    )

    val radialOverlay = Brush.radialGradient(
        colors = listOf(
            LightBlue.copy(alpha = 0.8f),
            Color.Transparent
        ),
        center = Offset(screenWidth / 2, screenHeight / 2), // Ekranın tam ortası
        radius = screenHeight / 5 // Radius'u ekran boyutuna göre ayarladım
    )

    val fjallaOne = FontFamily(
        Font(R.font.fjallaoneregular, FontWeight.Normal)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(radialOverlay),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {

                Text(
                    text = "STORY TIME",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = fjallaOne,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Learn English by writing stories! Use selected words to craft your own stories and share them with everyone.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontFamily = fjallaOne,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                Button(
                    onClick = {
                        navController.navigate("signup")
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightYellow,
                        contentColor = Pink
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontFamily = fjallaOne,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
