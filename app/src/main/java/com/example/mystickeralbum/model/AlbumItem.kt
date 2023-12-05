package com.example.mystickeralbum.model

import com.example.mystickeralbum.model.Album

data class AlbumItem(
    val album: Album,
    val editMode: Boolean = false
)
