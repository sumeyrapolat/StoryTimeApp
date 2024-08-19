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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.englishnotebook.R
import com.example.englishnotebook.ui.components.CategoryTabs
import com.example.englishnotebook.ui.components.StoryCard
import com.example.englishnotebook.ui.components.WordsCard
import com.example.englishnotebook.viewmodel.AuthViewModel
import com.example.englishnotebook.viewmodel.FeedViewModel
import com.example.englishnotebook.model.Story
import com.example.englishnotebook.ui.components.SearchBarComponent


@Composable
fun FeedScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel(), viewModel: FeedViewModel = hiltViewModel()) {

    val words by viewModel.words.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val postState by viewModel.postState.collectAsState()

    var selectedCategory by remember { mutableStateOf("Stories") }
    val userLoggedIn by authViewModel.userLoggedInState.collectAsState()

    var searchText = remember { mutableStateOf("") }
    val filteredStories = remember(stories) { mutableStateOf(stories) } // Başlangıçta tüm hikayeler

    // Arama işlevi
    fun performSearch() {
        val query = searchText.value.lowercase().trim()
        filteredStories.value = if (query.isEmpty()) {
            stories // Arama yapılmazsa tüm hikayeleri göster
        } else {
            stories.filter { story ->
                story.usedWords.any { word ->
                    word.lowercase().contains(query)
                }
            }
        }
    }

    // `stories` her değiştiğinde arama fonksiyonunu tekrar çalıştır
    LaunchedEffect(stories) {
        performSearch()
    }

    LaunchedEffect(userLoggedIn) {
        if (userLoggedIn) {
            viewModel.fetchStoriesFromFirestore()
            viewModel.fetchWords()
        } else {
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
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedCategory == "Stories") {
                SearchBarComponent(
                    searchText = searchText,
                    onSearch = {
                        performSearch() // Arama fonksiyonunu çağır
                        Log.d("FeedScreen", "Searching for: ${searchText.value}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                )
            }
            CategoryTabs(onCategorySelected = { category ->
                selectedCategory = category
                Log.d("FeedScreen", "Seçilen kategori: $category")
            })

            when (postState) {
                is FeedViewModel.PostState.Loading -> {
                    // Yükleme sırasında CircularProgressIndicator göster
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is FeedViewModel.PostState.Success -> {
                    when (selectedCategory) {
                        "Stories" -> {
                            if (filteredStories.value.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Henüz bir hikaye eklenmedi.",
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(filteredStories.value) { story ->
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
                        }
                        "Words" -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(words) { wordsGroup ->
                                    WordsCard(usedWords = wordsGroup, onAddClick = {
                                        navController.navigate("addstory/${wordsGroup.joinToString(",")}")
                                    })
                                }
                            }
                        }
                    }
                }
                is FeedViewModel.PostState.Error -> {
                    // Hata durumu için bir mesaj göster
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Bir hata oluştu: ${(postState as FeedViewModel.PostState.Error).errorMessage}",
                            color = Color.Red,
                            fontSize = 18.sp
                        )
                    }
                }
                else -> {
                    // Başka durumlar için gerekli işlemleri burada tanımlayabilirsiniz
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
