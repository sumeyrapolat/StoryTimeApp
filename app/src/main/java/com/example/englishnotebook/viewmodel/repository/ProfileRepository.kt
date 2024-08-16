package com.example.englishnotebook.viewmodel.repository

import com.example.englishnotebook.model.Post
import com.example.englishnotebook.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    // Kullanıcının gönderilerini alır
    suspend fun getPostsByUserId(userId: String): Result<List<Post>> {
        return try {
            val documents = db.collection("Posts")
                .document("PostDoc")
                .collection("UserPosts")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val posts = documents.mapNotNull { it.toObject(Post::class.java) }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Kullanıcının profil bilgilerini alır
    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val document = db.collection("Users")
                .document(userId)
                .get()
                .await()

            val userProfile = document.toObject(User::class.java)
            if (userProfile != null) {
                Result.success(userProfile)
            } else {
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
