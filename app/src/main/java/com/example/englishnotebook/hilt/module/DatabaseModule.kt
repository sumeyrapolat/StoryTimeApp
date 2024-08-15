package com.example.englishnotebook.hilt.module

import android.content.Context
import androidx.room.Room
import com.example.englishnotebook.room.dao.WordsDao
import com.example.englishnotebook.room.db.WordsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): WordsDatabase {
        return Room.databaseBuilder(
            appContext,
            WordsDatabase::class.java,
            "words_database"
        ).build()
    }

    @Provides
    fun provideWordsDao(database: WordsDatabase): WordsDao {
        return database.wordsDao()
    }
}