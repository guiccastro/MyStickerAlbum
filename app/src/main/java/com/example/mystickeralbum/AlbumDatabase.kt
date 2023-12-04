package com.example.mystickeralbum

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mystickeralbum.model.Album

@Database(
    version = 1,
    entities = [Album::class]
)
@TypeConverters(DatabaseConverters::class)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
}