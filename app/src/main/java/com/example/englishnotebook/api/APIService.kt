package com.example.englishnotebook.api

import retrofit2.http.GET

interface APIService {
    @GET("https://raw.githubusercontent.com/sumeyrapolat/StoryTimeWordsExtraction/main/extracted_words.csv")
    suspend fun getWordsCSV(): String
}