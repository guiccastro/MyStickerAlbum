package com.devgc.mystickeralbum.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devgc.mystickeralbum.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM Album")
    fun getAll(): List<Album>

    @Query("SELECT * FROM Album")
    fun getAllFlow(): Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM Album WHERE name = :albumName")
    fun getAlbumByName(albumName: String): Album?

    @Query("SELECT * FROM Album WHERE name = :albumName")
    fun getAlbumByNameFlow(albumName: String): Flow<Album?>
}