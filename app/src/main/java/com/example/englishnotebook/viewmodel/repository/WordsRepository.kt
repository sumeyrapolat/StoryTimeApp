package com.example.englishnotebook.viewmodel.repository

import com.example.englishnotebook.room.dao.WordsDao
import com.example.englishnotebook.room.entity.WordEntity
import javax.inject.Inject

class WordsRepository @Inject constructor(
    private val wordsDao: WordsDao
) {

    suspend fun insertWords(words: List<WordEntity>) {
        wordsDao.insertWords(words)
    }

    suspend fun getAllWords(): List<WordEntity> {
        return wordsDao.getAllWords()
    }

    suspend fun deleteWords() {
        wordsDao.deleteWords()
    }
}