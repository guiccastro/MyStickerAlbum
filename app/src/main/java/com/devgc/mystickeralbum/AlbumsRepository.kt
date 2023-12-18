package com.devgc.mystickeralbum

import com.devgc.mystickeralbum.model.Album

object AlbumsRepository {
    private val albumDao = MyStickerAlbumApplication.getDatabase().albumDao()

    fun getAllAlbums(): List<Album> {
        return albumDao.getAll()
    }

    fun addAlbum(album: Album) {
        albumDao.insert(album)
    }

    fun updateAlbum(newAlbum: Album, oldAlbum: Album) {
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
}