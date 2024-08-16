package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.api.RetrofitInstance
import com.example.englishnotebook.model.Post
import com.example.englishnotebook.model.Story
import com.example.englishnotebook.room.entity.WordEntity
import com.example.englishnotebook.viewmodel.repository.PostsRepository
import com.example.englishnotebook.viewmodel.repository.WordsRepository
import com.google.firebase.auth.FirebaseAuth
import com.opencsv.CSVReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.StringReader
import javax.inject.Inject



@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: WordsRepository,
    private val postsRepository: PostsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _words = MutableStateFlow<List<List<String>>>(emptyList())
    val words: StateFlow<List<List<String>>> = _words

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState

    init {
        fetchStoriesFromFirestore()
    }


    fun fetchStoriesFromFirestore() {
        viewModelScope.launch {
            val postsResult = postsRepository.getAllPosts()
            if (postsResult.isSuccess) {
                val posts = postsResult.getOrNull().orEmpty()

                // Tüm kullanıcı profillerini asenkron olarak çekeriz.
                val stories = posts.map { post ->
                    async {
                        val userProfileResult = postsRepository.getUserProfile(post.userId)
                        userProfileResult.getOrNull()?.let { userProfile ->
                            Story(
                                userPhoto = userProfile.profilePhotoUrl ?: "",
                                userName = "${userProfile.firstName} ${userProfile.lastName}",
                                title = post.title,
                                content = post.content,
                                usedWords = post.usedWords,
                                timestamp = post.timestamp,
                                userEmail = userProfile.email
                            )
                        }
                    }
                }.awaitAll().filterNotNull() // Null olanları filtrele

                _stories.value = stories
            } else {
                Log.e("FeedViewModel", "Error fetching stories")
            }
        }
    }

    fun fetchWords() {
        viewModelScope.launch {
            try {
                if (repository.getAllWords().isEmpty()) {
                    Log.d("FeedViewModel", "Fetching words from API")
                    val csvData = RetrofitInstance.apiService.getWordsCSV()
                    val csvReader = CSVReader(StringReader(csvData))

                    csvReader.readNext() // Başlık satırını atla

                    val wordLists = csvReader.readAll().map { it.toList() }
                    val groupedWordLists: List<List<String>> = wordLists.chunked(size = 12).map { it.flatten() }

                    groupedWordLists.forEach { group ->
                        val wordEntities = group.map { WordEntity(word = it) }
                        repository.insertWords(wordEntities)
                    }

                    _words.value = groupedWordLists

                } else {
                    Log.d("FeedViewModel", "Fetching words from database")
                    val wordsFromDb = repository.getAllWords()
                    val groupedWords = wordsFromDb.map { it.word }.chunked(12)
                    _words.value = groupedWords
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error fetching words: ${e.message}")
            }
        }
    }

    fun savePost(title: String, content: String, usedWords: List<String>) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            _postState.value = PostState.Error("User not logged in")
            return
        }

        val post = Post(
            userId = currentUser.uid,
            title = title,
            content = content,
            usedWords = usedWords
        )

        _postState.value = PostState.Loading

        viewModelScope.launch {
            val result = postsRepository.addPost(post, currentUser.uid)
            if (result.isSuccess) {
                _postState.value = PostState.Success
            } else {
                _postState.value = PostState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _postState.value = PostState.Idle
    }

    sealed class PostState {
        object Idle : PostState()
        object Loading : PostState()
        object Success : PostState()
        data class Error(val errorMessage: String) : PostState()
    }
}