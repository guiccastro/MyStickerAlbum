package com.example.mystickeralbum.ui.stateholders

import com.example.mystickeralbum.model.Album

data class AlbumsListUIState(
    val albumsList: List<Album> = emptyList(),
    val onAlbumClick: (Album) -> Unit = {},
    val updateAlbumsList: () -> Unit = {}
)
