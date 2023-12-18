package com.devgc.mystickeralbum.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devgc.mystickeralbum.model.Album

@Database(
    version = 1,
    entities = [Album::class]
)
@TypeConverters(DatabaseConverters::class)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
}