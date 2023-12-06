package com.example.mystickeralbum.stateholders

import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.Sticker
import com.example.mystickeralbum.model.StickersList

data class UpdateAlbumUIState(
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onReceivedAlbumName: (String) -> Unit = {},
    val onStickerClick: (Sticker) -> Unit = {},
    val showDialog: Boolean = false,
    val onFoundClick: (Sticker) -> Unit = {},
    val onNotFoundClick: (Sticker) -> Unit = {},
    val onChangeRepeatedStickerClick: (Sticker, Int) -> Unit = { _, _ -> },
)
