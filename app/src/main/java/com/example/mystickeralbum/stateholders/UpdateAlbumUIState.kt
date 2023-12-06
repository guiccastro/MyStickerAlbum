package com.example.mystickeralbum.stateholders

import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList

data class UpdateAlbumUIState(
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onReceivedAlbumName: (String) -> Unit = {},
    val onStickerClick: (Sticker) -> Unit = {},
    val onCloseDialog: () -> Unit = {},
    val stickerDialog: Sticker = Sticker("", false, 0),
    val showDialog: Boolean = false,
    val onFoundNotFoundClick: (Boolean) -> Unit = {},
    val onChangeRepeatedStickerClick: (Int) -> Unit = {},
)
