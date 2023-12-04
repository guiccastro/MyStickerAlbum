package com.example.mystickeralbum

import com.example.mystickeralbum.model.Album

data class AlbumsUIState(
    val albumsList: List<Album> = emptyList()
)
