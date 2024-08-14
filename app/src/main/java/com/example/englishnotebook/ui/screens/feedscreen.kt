package com.example.englishnotebook.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.englishnotebook.R
import com.example.englishnotebook.ui.components.CategoryTabs
import com.example.englishnotebook.ui.components.StoryCard
import com.example.englishnotebook.ui.components.WordsCard
import com.example.englishnotebook.viewmodel.AuthViewModel
import com.example.englishnotebook.viewmodel.FeedViewModel

@Composable
fun FeedScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel(), viewModel: FeedViewModel = hiltViewModel()) {

    val words by viewModel.words.collectAsState()

    // Eğer veri henüz yüklenmediyse
    LaunchedEffect(Unit) {
        viewModel.fetchWords()
    }

    var selectedCategory by remember { mutableStateOf("Stories") }

    val userLoggedIn by authViewModel.userLoggedInState.collectAsState()

    val stories = listOf(
        Story(R.drawable.ic_launcher_foreground, "User1", "Story Title 1", "This is the story content. It can be very long and should be truncated if it exceeds 8 lines.", listOf("Word1", "Word2")),
        Story(R.drawable.ic_launcher_foreground, "User2", "Story Title 2", "Another story content that should also be truncated if it is too long.", listOf("Word3", "Word4")),
        Story(R.drawable.ic_launcher_foreground, "User3", "Story Title 3", "Yet another story content with a lot of text that needs to be truncated after 8 lines.", listOf("Word5", "Word6")),
        Story(R.drawable.ic_launcher_foreground, "User4", "Story Title 4", "Yet another story content with a lot of text that needs to beYet another story content with a lot of text that needs to be trYet another story content with a lot of text that needs to be trYet another story content with a lot of text that needs to be trYet another story content with a lot of text that needs to be trYet another story content with a lot of text that needs to be tr truncated after 8 lines.", listOf("Word5", "Word6"))
    )


    LaunchedEffect(userLoggedIn) {
        if (!userLoggedIn) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    if (userLoggedIn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Gradient arka plan uygulandı
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoryTabs(onCategorySelected = { category ->
                selectedCategory = category
                Log.d("FeedScreen", "Seçilen kategori: $category")
            })

            when (selectedCategory) {
                "Stories" -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(stories) { story ->
                            StoryCard(
                                userPhoto = story.userPhoto,
                                userName = story.userName,
                                storyTitle = story.title,
                                storyContent = story.content,
                                usedWords = story.usedWords
                            )
                        }
                    }
                }
                "Words" -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // 2 sütunlu grid
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(words) { wordsGroup ->
                            WordsCard(usedWords = wordsGroup, onAddClick = {
                                navController.navigate("addstory")
                            })
                        }
                    }
                }
            }
        }
    } else {
        // Yükleme ekranı veya boş bir ekran gösterebilirsiniz
        CircularProgressIndicator()
    }
}

data class Story(
    val userPhoto: Int, // Resource ID for user photo
    val userName: String,
    val title: String,
    val content: String,
    val usedWords: List<String>
)
