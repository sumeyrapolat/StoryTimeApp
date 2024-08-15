package com.example.englishnotebook.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.englishnotebook.room.dao.WordsDao
import com.example.englishnotebook.room.entity.WordEntity

@Database(entities = [WordEntity::class], version = 1)
abstract class WordsDatabase : RoomDatabase() {

    abstract fun wordsDao(): WordsDao

}