package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.model.Story
import com.example.englishnotebook.model.User
import com.example.englishnotebook.viewmodel.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Kullanıcı profili için StateFlow
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    // Kullanıcı gönderileri için StateFlow
    private val _userPosts = MutableStateFlow<List<Story>>(emptyList())
    val userPosts: StateFlow<List<Story>> = _userPosts

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState

    init {
        fetchUserProfileAndPosts()
    }

    fun fetchUserProfileAndPosts() {
        viewModelScope.launch {
            _postState.value = PostState.Loading
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val postsResult = profileRepository.getPostsByUserId(userId)
                val userProfileResult = profileRepository.getUserProfile(userId)

                // Hata mesajlarını daha detaylı loglamak için
                if (postsResult.isFailure || userProfileResult.isFailure) {
                    val postError = postsResult.exceptionOrNull()?.message ?: "Unknown post error"
                    val profileError = userProfileResult.exceptionOrNull()?.message ?: "Unknown profile error"
                    _postState.value = PostState.Error("Error fetching stories or user profile: Post Error: $postError, Profile Error: $profileError")
                    Log.e("ProfileViewModel", "Error fetching stories or user profile: Post Error: $postError, Profile Error: $profileError")
                    return@launch
                }

                if (postsResult.isSuccess && userProfileResult.isSuccess) {
                    val posts = postsResult.getOrNull().orEmpty()
                    val userProfile = userProfileResult.getOrNull()

                    Log.d("ProfileViewModel", "Fetched posts: $posts")
                    Log.d("ProfileViewModel", "Fetched user profile: $userProfile")

                    if (userProfile != null) {
                        val stories = posts.map { post ->
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
                        _userPosts.value = stories
                        _userProfile.value = userProfile
                        _postState.value = PostState.Success
                    }
                }
            }
        }
    }

    sealed class PostState {
        object Idle : PostState()
        object Loading : PostState()
        object Success : PostState()
        data class Error(val errorMessage: String) : PostState()
    }
}
