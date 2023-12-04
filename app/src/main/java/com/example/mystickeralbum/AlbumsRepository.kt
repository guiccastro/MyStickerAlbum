package com.example.mystickeralbum

import com.example.mystickeralbum.model.Album

object AlbumsRepository {
    private val albumDao = MyStickerAlbumApplication.getDatabase().albumDao()

    fun getAllAlbums(): List<Album> {
        return albumDao.getAll()
    }

    fun addAlbum(album: Album) {
        albumDao.insert(album)
    }
}