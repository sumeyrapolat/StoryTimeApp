package com.example.englishnotebook.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.englishnotebook.ui.theme.LightPink
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.cardGradientColor

@Composable
fun StoryCard(
    userPhoto: String,
    userName: String,
    storyTitle: String,
    storyContent: String,
    usedWords: List<String>
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(2.dp, cardGradientColor , shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true }, // Tıklama işlevi ekledik
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = userPhoto),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userName,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = storyTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = storyContent,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = usedWords.joinToString(", "),
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // Eğer tıklanırsa tüm içerikleri gösteren bir dialog açılır
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = storyTitle, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxHeight(0.8f)) { // Yüksekliği sınırlandırabilirsiniz
                    item {
                        Text(text = storyContent, fontSize = 16.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = "Close",
                        fontSize = 16.sp,
                        color = Pink,
                        fontWeight = FontWeight.SemiBold

                    )
                }
            },
            containerColor = LightPink

        )
    }
}
