package com.example.englishnotebook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishnotebook.api.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.StringReader
import com.opencsv.CSVReader
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _words = MutableStateFlow<List<List<String>>>(emptyList())
    val words: StateFlow<List<List<String>>> = _words


    fun fetchWords() {
        viewModelScope.launch {
            try {
                val csvData = RetrofitInstance.apiService.getWordsCSV()
                val csvReader = CSVReader(StringReader(csvData))

                csvReader.readNext()

                val wordLists = csvReader.readAll().map { it.toList() }
                _words.value = wordLists
            } catch (e: Exception) {

                Log.e("FeedViewModel", "Error fetching words: ${e.message}")
            }
        }
    }
}