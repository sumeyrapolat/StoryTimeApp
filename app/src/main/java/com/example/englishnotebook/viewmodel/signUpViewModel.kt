package com.example.englishnotebook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor( private val auth: FirebaseAuth ) : ViewModel() {

    // Kayıt durumu için StateFlow kullanımı
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState = _signUpState.asStateFlow()


    fun signUp(email: String, password: String) {
        _signUpState.value = SignUpState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signUpState.value = SignUpState.Success("Sign up successful!")
                    } else {
                        _signUpState.value = SignUpState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }



}

// Kayıt durumu için durum sınıfları
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val message: String) : SignUpState()
    data class Error(val error: String) : SignUpState()
}