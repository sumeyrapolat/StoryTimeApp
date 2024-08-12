package com.example.englishnotebook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor( private val auth: FirebaseAuth) : ViewModel(){

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState = _signInState.asStateFlow()

    fun signIn(email: String, password: String) {
        _signInState.value = SignInState.Loading
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _signInState.value = SignInState.Success("Sign in successful!")
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "Unknown error")
            }
        }
    }



}

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Success(val message: String) : SignInState()
    data class Error(val error: String) : SignInState()
}