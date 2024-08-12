package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor( private val auth: FirebaseAuth, private val db: FirebaseFirestore) : ViewModel(){

    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState


    private val _userDataState = MutableStateFlow<UserDataState>(UserDataState.Loading)
    val userDataState: StateFlow<UserDataState> = _userDataState

    init {
        fetchUserData()
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _passwordResetState.value = PasswordResetState.Loading
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _passwordResetState.value = PasswordResetState.Success("Password reset email sent")
                        Log.d("AuthViewModel", "Password reset email sent successfully.")
                    } else {
                        val errorMessage = task.exception?.message ?: "Failed to send password reset email"
                        _passwordResetState.value = PasswordResetState.Error(errorMessage)
                        Log.e("AuthViewModel", "Error sending password reset email: $errorMessage")
                    }
                }
        }
    }


    fun signOut() {
        auth.signOut()
    }

    fun resetPasswordResetState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

    private fun fetchUserData() {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            Log.d("AuthViewModel", "Fetching user data for userId: $userEmail") // Log eklendi
            db.collection("Users").document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val email = document.getString("email") ?: ""
                        Log.d("AuthViewModel", "User data fetched: $firstName $lastName, $email") // Log eklendi
                        _userDataState.value = UserDataState.Success(firstName, lastName, email)
                    } else {
                        Log.e("AuthViewModel", "Document does not exist for userId: $userEmail") // Log eklendi
                        _userDataState.value = UserDataState.Error("Document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AuthViewModel", "Failed to fetch user data: ${exception.message}") // Log eklendi
                    _userDataState.value = UserDataState.Error(exception.message ?: "Failed to fetch user data")
                }
        } else {
            Log.e("AuthViewModel", "User not logged in") // Log eklendi
            _userDataState.value = UserDataState.Error("User not logged in")
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _passwordResetState.value = PasswordResetState.Loading
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _passwordResetState.value = PasswordResetState.Success("Password reset email sent")
                    } else {
                        _passwordResetState.value = PasswordResetState.Error("Failed to send password reset email")
                    }
                }
        }
    }
}

sealed class PasswordResetState {
    object Idle : PasswordResetState()
    object Loading : PasswordResetState()
    data class Success(val message: String) : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}

sealed class UserDataState {
    object Loading : UserDataState()
    data class Success(val firstName: String, val lastName: String, val email: String) : UserDataState()
    data class Error(val message: String) : UserDataState()
}