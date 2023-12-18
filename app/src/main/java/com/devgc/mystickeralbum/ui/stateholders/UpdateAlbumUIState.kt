package com.devgc.mystickeralbum.ui.stateholders

import android.content.Context
import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.Sticker
import com.devgc.mystickeralbum.model.StickersList

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
