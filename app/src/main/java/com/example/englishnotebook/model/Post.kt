package com.example.englishnotebook.model



data class Post(
    val title: String = "",
    val content: String = "",
    val usedWords: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
