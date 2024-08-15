package com.example.englishnotebook.model

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profilePhotoUrl: String? = null
)
