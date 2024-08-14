package com.example.englishnotebook.viewmodel.repository

import android.util.Log
import com.example.englishnotebook.model.SignUpUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private var cachedUserData: SignUpUser? = null


    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadUserData(userID: String): Result<SignUpUser> {
        return try {
            val document = db.collection("Users").document(userID).get().await()
            Log.d("AuthRepository", "Document snapshot: $document")
            Log.d("AuthRepository", "Fetching data for userID: $userID")

            if (document.exists()) {
                val userData = document.toObject(SignUpUser::class.java)
                Log.d("AuthRepository", "User data: $userData")

                userData?.let {
                    cachedUserData = it  // Veriyi kaydet
                    Log.d("AuthRepository", "Cached User Data: $cachedUserData") // Log the cached data
                    Result.success(it)
                } ?: Result.failure(Exception("User data is null"))
            } else {
                Result.failure(Exception("User data not found"))
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("AuthRepository", "Firestore error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("AuthRepository", "General error: ${e.message}")
            Result.failure(e)
        }
    }

    fun getCachedUserData(): SignUpUser? {
        return cachedUserData
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Failed to send password reset email")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun checkUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserID(): String? {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("AuthRepository", "No user is currently logged in.")
            return null
        }
        return currentUser.uid
    }

}

