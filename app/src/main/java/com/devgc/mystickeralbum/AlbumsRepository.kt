package com.devgc.mystickeralbum

import com.devgc.mystickeralbum.model.Album
import kotlinx.coroutines.flow.Flow

object AlbumsRepository {
    private val albumDao = MyStickerAlbumApplication.getDatabase().albumDao()

    fun getAllAlbums(): List<Album> {
        return albumDao.getAll()
    }

    fun getAllAlbumsFlow(): Flow<List<Album>> {
        return albumDao.getAllFlow()
    }

    fun insertAlbum(album: Album) {
        albumDao.insert(album)
    }

    fun replaceAlbum(newAlbum: Album, oldAlbum: Album) {
        albumDao.delete(oldAlbum)
        albumDao.insert(newAlbum)
    }

    fun updateStickers(album: Album) {
        albumDao.insert(album)
    }

    fun removeAlbum(album: Album) {
        albumDao.delete(album)
    }

    fun getAlbumByName(albumName: String): Album? {
        return albumDao.getAlbumByName(albumName)
    }

    fun getAlbumByNameFlow(albumName: String): Flow<Album?> {
        return albumDao.getAlbumByNameFlow(albumName)
    }
}