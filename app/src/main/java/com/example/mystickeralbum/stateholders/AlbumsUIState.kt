package com.example.mystickeralbum.stateholders

import android.app.Activity
import com.example.mystickeralbum.model.Album

data class AlbumsUIState(
    val albumsList: List<Album> = emptyList(),
    val onFabClick: (Activity) -> Unit = {},
    val onAlbumClick: (Activity, Album) -> Unit = { _, _ -> }
)
