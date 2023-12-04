package com.example.mystickeralbum.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystickeralbum.model.Album

@Dao
interface AlbumDao {

    @Query("SELECT * FROM Album")
    fun getAll(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)
}