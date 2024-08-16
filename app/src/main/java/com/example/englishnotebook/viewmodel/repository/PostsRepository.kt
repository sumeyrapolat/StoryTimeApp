package com.example.englishnotebook.viewmodel.repository

import com.example.englishnotebook.model.Post
import com.example.englishnotebook.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class PostsRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun addPost(post: Post, userId: String): Result<Unit> {
        return try {
            db.collection("Posts")
                .document("PostDoc")
                .collection("UserPosts")
                .add(post)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getAllPosts(): Result<List<Post>> {
        return try {
            val documents = db.collection("Posts")
                .document("PostDoc")
                .collection("UserPosts")
                .get()
                .await()

            val posts = documents.mapNotNull { it.toObject(Post::class.java) }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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