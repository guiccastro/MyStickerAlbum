package com.example.mystickeralbum

import android.app.Application
import androidx.room.Room
import com.example.mystickeralbum.database.AlbumDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyStickerAlbumApplication : Application() {

    val database = Room.databaseBuilder(
        applicationContext,
        AlbumDatabase::class.java, "albums-database"
    ).build()
}