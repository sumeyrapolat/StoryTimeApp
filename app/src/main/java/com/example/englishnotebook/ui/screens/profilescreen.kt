package com.example.englishnotebook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.englishnotebook.model.Story
import com.example.englishnotebook.ui.theme.LightPink
import com.example.englishnotebook.ui.theme.LightPurple
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.PastelPink
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.SoftBlue
import com.example.englishnotebook.ui.theme.SoftGreen
import com.example.englishnotebook.ui.theme.SoftPink
import com.example.englishnotebook.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {

    val userProfile = viewModel.userProfile.collectAsState().value
    val userPosts = viewModel.userPosts.collectAsState().value

    var selectedPost by remember { mutableStateOf<Story?>(null) }

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            LightPurple,
            SoftPink,

        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, 0f),
        tileMode = TileMode.Clamp
    )

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfileAndPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Kullanıcı Bilgileri ve İstatistikler
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kullanıcı Fotoğrafı
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = rememberImagePainter(data = userProfile?.profilePhotoUrl),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(5.dp))
                // Kullanıcı Adı
                Text(
                    text = "${userProfile?.firstName} ${userProfile?.lastName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Takipçi, Gönderi ve Takip Bilgileri
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileStatistic(label = "posts", value = 1)
                    ProfileStatistic(label = "followers", value = 1)
                    ProfileStatistic(label = "following", value = 1)
                }
            }
        }

        // Alt çizgi
        Spacer(modifier = Modifier.height(12.dp))

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        // Kullanıcının Gönderileri Bölümü (Dikey Liste içinde başlıklar)
        if (userPosts.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(userPosts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(2.dp, backgroundGradient, shape = RoundedCornerShape(10.dp))
                            .clickable { selectedPost = post },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = post.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    text = post.content,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Eğer gönderi yoksa bilgilendirici bir mesaj gösterilebilir
            Text(
                text = "Henüz bir hikaye eklenmedi.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

    }

    // Detay Görüntüleme Diyaloğu
    if (selectedPost != null) {
        PostDetailDialog(post = selectedPost!!, onDismiss = { selectedPost = null })
    }
}

@Composable
fun PostDetailDialog(post: Story, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    fontSize = 16.sp,
                    color = Pink,
                    fontWeight = FontWeight.SemiBold

                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(2.dp),
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "${post.usedWords.joinToString(", ")}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        },
        containerColor = LightPink
    )
}

@Composable
fun ProfileStatistic(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
