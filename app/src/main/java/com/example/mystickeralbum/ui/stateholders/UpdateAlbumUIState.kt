package com.example.mystickeralbum.ui.stateholders

import android.content.Context
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList

data class UpdateAlbumUIState(
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onStickerClick: (Sticker) -> Unit = {},
    val onCloseStickerDialog: () -> Unit = {},
    val stickerDialog: Sticker = Sticker("", false, 0),
    val showStickerDialog: Boolean = false,
    val onFoundNotFoundClick: (Boolean) -> Unit = {},
    val onChangeRepeatedStickerClick: (Int) -> Unit = {},
    val showDeleteAlbumDialog: Boolean = false,
    val onCloseDeleteAlbumDialog: () -> Unit = {},
    val onConfirmDeleteAlbumDialog: () -> Unit = {},
    val onCopyMissingStickersClick: (Context) -> Unit = {},
    val onCopyRepeatedStickersClick: (Context) -> Unit = {}
)
