package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.api.RetrofitInstance
import com.example.englishnotebook.room.entity.WordEntity
import com.example.englishnotebook.viewmodel.repository.WordsRepository
import com.opencsv.CSVReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.StringReader
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: WordsRepository
) : ViewModel() {

    private val _words = MutableStateFlow<List<List<String>>>(emptyList())
    val words: StateFlow<List<List<String>>> = _words

    fun fetchWords() {
        viewModelScope.launch {
            try {
                // Eğer veritabanında veri yoksa API'den çek
                if (repository.getAllWords().isEmpty()) {
                    Log.d("FeedViewModel", "Fetching words from API")
                    val csvData = RetrofitInstance.apiService.getWordsCSV()
                    val csvReader = CSVReader(StringReader(csvData))

                    csvReader.readNext() // Başlık satırını atla

                    val wordLists = csvReader.readAll().map { it.toList() }
                    val groupedWordLists: List<List<String>> = wordLists.chunked(size = 12).map { it.flatten() } // Kelimeleri 12'lik gruplara ayır ve düzleştir

                // Veritabanına 12'lik gruplar halinde kaydet
                    groupedWordLists.forEach { group ->
                        val wordEntities = group.map { WordEntity(word = it) }
                        repository.insertWords(wordEntities)
                    }

                    _words.value = groupedWordLists

                } else {
                    // Veritabanındaki veriyi yükle
                    Log.d("FeedViewModel", "Fetching words from database")
                    val wordsFromDb = repository.getAllWords()
                    val groupedWords = wordsFromDb.map { it.word }.chunked(12)
                    _words.value = groupedWords
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error fetching words: ${e.message}")
            }
        }
    }
}

