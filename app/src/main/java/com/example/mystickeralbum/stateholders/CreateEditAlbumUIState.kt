package com.example.mystickeralbum.stateholders

import android.app.Activity
import com.example.mystickeralbum.model.Album
import com.example.mystickeralbum.model.AlbumStatus
import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.model.StickersList
import com.example.mystickeralbum.model.TextFieldValues

data class CreateEditAlbumUIState(
    val albumNameTextField: TextFieldValues = TextFieldValues(),
    val albumImageUrlTextField: TextFieldValues = TextFieldValues(),
    val normalStickersFromTextField: TextFieldValues = TextFieldValues(),
    val normalStickersToTextField: TextFieldValues = TextFieldValues(),
    val specialStickersLetterFromTextField: TextFieldValues = TextFieldValues(),
    val specialStickersLetterToTextField: TextFieldValues = TextFieldValues(),
    val specialStickersNumberFromTextField: TextFieldValues = TextFieldValues(),
    val specialStickersNumberToTextField: TextFieldValues = TextFieldValues(),
    val hasSpecialStickers: Boolean = false,
    val onHasSpecialStickersChange: (Boolean) -> Unit = {},
    val specialStickerType: SpecialStickerType = SpecialStickerType.LetterNumber,
    val onSpecialStickerTypeChange: (SpecialStickerType) -> Unit = {},
    val onCreateEditClick: (Activity) -> Unit = { },
    val totalStickers: Int = 0,
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val onReceivedAlbumName: (String) -> Unit = {},
    val isCreateAlbum: Boolean = true
)
