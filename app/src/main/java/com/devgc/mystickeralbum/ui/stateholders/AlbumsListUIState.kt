package com.devgc.mystickeralbum.ui.stateholders

import com.devgc.mystickeralbum.model.Album

data class AlbumsListUIState(
    val albumsList: List<Album> = emptyList(),
    val onAlbumClick: (Album) -> Unit = {},
    val updateAlbumsList: () -> Unit = {}
)
