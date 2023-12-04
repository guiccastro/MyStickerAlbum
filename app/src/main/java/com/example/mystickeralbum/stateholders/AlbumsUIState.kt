package com.example.mystickeralbum.stateholders

import com.example.mystickeralbum.model.Album

data class AlbumsUIState(
    val albumsList: List<Album> = emptyList()
)
