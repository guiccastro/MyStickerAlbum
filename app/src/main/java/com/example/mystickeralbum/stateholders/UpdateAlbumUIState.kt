package com.example.mystickeralbum.stateholders

import android.app.Activity
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList

data class UpdateAlbumUIState(
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onReceivedAlbumName: (String) -> Unit = {},
    val onStickerClick: (Sticker) -> Unit = {},
    val onCloseStickerDialog: () -> Unit = {},
    val stickerDialog: Sticker = Sticker("", false, 0),
    val showStickerDialog: Boolean = false,
    val onFoundNotFoundClick: (Boolean) -> Unit = {},
    val onChangeRepeatedStickerClick: (Int) -> Unit = {},
    val onDeleteAlbumClick: () -> Unit = {},
    val showDeleteAlbumDialog: Boolean = false,
    val onCloseDeleteAlbumDialog: () -> Unit = {},
    val onConfirmDeleteAlbumDialog: (Activity) -> Unit = {},
    val onNewScreenCall: (Activity) -> Unit = {},
    val onEditAlbumClick: (Activity) -> Unit = {}
)
