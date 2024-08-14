package com.example.englishnotebook.model

data class SignUpUser(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profilePhotoUrl: String? = null // Profil fotoğrafı URL'si alanı eklendi
)
