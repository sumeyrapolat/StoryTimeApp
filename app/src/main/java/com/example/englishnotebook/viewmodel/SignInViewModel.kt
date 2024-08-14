package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.viewmodel.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState = _signInState.asStateFlow()

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        Log.d("SignInViewModel", "signIn called with email: $email")
        _signInState.value = SignInState.Loading
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            if (result.isSuccess) {
                Log.d("SignInViewModel", "signIn successful")
                onSuccess()
                authRepository.loadUserData(auth.currentUser!!.uid    )
                _signInState.value = SignInState.Success("Sign in successful!")
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                Log.d("SignInViewModel", "signIn failed with error: $errorMessage")
                _signInState.value = SignInState.Error(errorMessage)
            }
        }
    }

    fun resetSignInState() {
        _signInState.value = SignInState.Idle
    }
}

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Success(val message: String) : SignInState()
    data class Error(val error: String) : SignInState()
}
