package com.devgc.mystickeralbum

import android.app.Application
import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.devgc.mystickeralbum.database.AlbumDatabase
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

    lateinit var imageLoader: ImageLoader

    private val databaseName = "album-database"

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            getContext(),
            AlbumDatabase::class.java, databaseName
        ).build()
        LanguageRepository.initiateLanguage(baseContext)
        imageLoader = ImageLoader(this).newBuilder()
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    fun getContext(): Context {
        return getInstance().applicationContext
    }
}