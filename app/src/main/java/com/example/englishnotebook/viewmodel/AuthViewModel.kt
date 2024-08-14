package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.model.SignUpUser
import com.example.englishnotebook.viewmodel.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState

    private val _userDataState = MutableStateFlow<UserDataState>(UserDataState.Loading)
    val userDataState: StateFlow<UserDataState> = _userDataState

    private val _userLoggedInState = MutableStateFlow<Boolean>(false)
    val userLoggedInState: StateFlow<Boolean> = _userLoggedInState

    init {
        checkUserLoggedIn()
        // Eğer kullanıcı giriş yapmışsa, önbellekteki verilerin yüklü olup olmadığını kontrol ediyoruz.
        if (_userLoggedInState.value) {
            loadCachedUserDataIfNeeded()
        }
    }


    private fun checkUserLoggedIn() {
        _userLoggedInState.value = authRepository.checkUserLoggedIn()
    }

    // Bu fonksiyon, önbellekte verilerin olup olmadığını kontrol eder ve gerekirse onViewInitialized çağırır.
    fun onViewInitialized() {
        if (_userDataState.value !is UserDataState.Success) {
            loadUserData()
        }
    }
    // Bu fonksiyon, önbellekteki verileri kontrol eder ve state'e yükler.
    fun loadCachedUserDataIfNeeded() {
        val cachedData = authRepository.getCachedUserData()
        if (cachedData != null) {
            _userDataState.value = UserDataState.Success(
                firstName = cachedData.firstName,
                lastName = cachedData.lastName,
                email = cachedData.email
            )
            Log.d("AuthViewModel", "Cached data loaded: $cachedData")
        } else {
            // Eğer önbellekte veri yoksa, verileri yükle
            loadUserData()
        }
    }


    fun sendPasswordResetEmail(email: String) {
        _passwordResetState.value = PasswordResetState.Loading
        authRepository.sendPasswordResetEmail(
            email = email,
            onSuccess = {
                _passwordResetState.value = PasswordResetState.Success("Password reset email sent")
                Log.d("AuthViewModel", "Password reset email sent successfully.")
            },
            onError = { errorMessage ->
                _passwordResetState.value = PasswordResetState.Error(errorMessage)
                Log.e("AuthViewModel", "Error sending password reset email: $errorMessage")
            }
        )
    }

    fun signOut() {
        authRepository.signOut()
        _userDataState.value = UserDataState.Error("User not logged in")
        _userLoggedInState.value = false
        Log.d("AuthViewModel", "User signed out")
    }

    fun loadUserData() {
        val userID = authRepository.getCurrentUserID()
        Log.d("AuthViewModel", "Attempting to load user data for userID: $userID")
        if (userID != null) {
            viewModelScope.launch {
                val result = authRepository.loadUserData(userID)
                if (result.isSuccess) {
                    val userData = result.getOrNull()
                    Log.d("AuthViewModel", "LoadUserData result: $userData")
                    if (userData != null) {
                        Log.d("AuthViewModel", "User data successfully loaded.")
                        _userDataState.value = UserDataState.Success(
                            firstName = userData.firstName,
                            lastName = userData.lastName,
                            email = userData.email
                        )
                    } else {
                        Log.d("AuthViewModel", "User data is null.")
                        _userDataState.value = UserDataState.Error("User data is null")
                    }
                } else {
                    val errorMessage = when (result.exceptionOrNull()) {
                        is FirebaseAuthInvalidCredentialsException -> "Invalid credentials"
                        is FirebaseAuthInvalidUserException -> "User not found"
                        else -> "Unknown error"
                    }
                    Log.d("AuthViewModel", "Error loading user data: $errorMessage")
                    _userDataState.value = UserDataState.Error(errorMessage)
                }
            }
        } else {
            Log.d("AuthViewModel", "User not logged in.")
            _userDataState.value = UserDataState.Error("User not logged in")
        }
    }

    fun resetPasswordResetState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

    fun forgotPassword(email: String) {
        sendPasswordResetEmail(email)
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
}

