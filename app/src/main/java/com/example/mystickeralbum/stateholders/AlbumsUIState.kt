package com.example.mystickeralbum.stateholders

import android.app.Activity
import com.example.mystickeralbum.model.AlbumItem

data class AlbumsUIState(
    val albumsList: List<AlbumItem> = emptyList(),
    val onFabClick: (Activity) -> Unit = {},
    val onAlbumLongClick: (AlbumItem) -> Unit = {},
    val onDeleteClick: (AlbumItem) -> Unit = {},
    val onEditClick: (AlbumItem) -> Unit = {},
    val onCloseEditModeClick: (AlbumItem) -> Unit = {}
)
