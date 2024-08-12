package com.example.englishnotebook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishnotebook.R
import com.example.englishnotebook.ui.theme.LightBlue
import com.example.englishnotebook.ui.theme.LightOrange
import com.example.englishnotebook.ui.theme.LightPurple
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.PastelPink
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.SoftBlue
import com.example.englishnotebook.ui.theme.SoftGreen
import com.example.englishnotebook.ui.theme.SoftPink

@Composable
fun StoryCard(
    userPhoto: Int, // Resource ID for user photo
    userName: String,
    storyTitle: String,
    storyContent: String,
    usedWords: List<String>
) {
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            Pink,
            PastelPink,
            SoftGreen,
            PastelPink,
            Pink
        ),
        start = Offset(0f, 0f),
        end = Offset( Float.POSITIVE_INFINITY,0f),
        tileMode = TileMode.Clamp
    )


    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(2.dp, backgroundGradient, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Transparent yaparak arka planın gradient olmasını sağlıyoruz
        )
    ) {
        Column(
            modifier = Modifier
                //.background(backgroundGradient) // Gradient arka plan burada uygulanıyor
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = userPhoto),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = storyTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = storyContent,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 8,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = usedWords.joinToString(", "),
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun StoryCardPreview() {
    StoryCard(
        userPhoto = R.drawable.ic_launcher_foreground,
        userName = "Sarhoskedi",
        usedWords = listOf("word1", "word2", "word3"),
        storyContent = "dsfghfgfdsddsfdsfghfgfdsddsfghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsdfsghjgfhgsafsghjgfhgsaghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsadsfghfgfdsddsfdsfghfgfdsddsfghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsdfsghjgfhgsafsghjgfhgsaghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsfghfgfdsddsfdsfghfgfdsddsfghfgfdsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsadsdfsghjgfhgsafsghjgfhgsaghfgfdsdfsghjgfhgsafsghjgfhgsa",
        storyTitle = "title"
    )
}
