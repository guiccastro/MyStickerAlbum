package com.example.mystickeralbum

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mystickeralbum.database.AlbumDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyStickerAlbumApplication : Application() {

    companion object {
        private lateinit var instance: MyStickerAlbumApplication
        private lateinit var database: AlbumDatabase

        fun getInstance(): MyStickerAlbumApplication {
            return instance
        }

        fun getDatabase(): AlbumDatabase {
            return database
        }
    }

    private val databaseName = "album-database"

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            getContext(),
            AlbumDatabase::class.java, databaseName
        ).build()
        LanguageRepository.initiateLanguage(baseContext)
    }

    fun getContext(): Context {
        return getInstance().applicationContext
    }
}