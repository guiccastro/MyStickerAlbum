package com.devgc.mystickeralbum.ui.stateholders

import com.devgc.mystickeralbum.model.Album
import com.devgc.mystickeralbum.model.AlbumStatus
import com.devgc.mystickeralbum.model.SpecialStickerType
import com.devgc.mystickeralbum.model.StickersList
import com.devgc.mystickeralbum.model.TextFieldValues

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
    val onCreateEditClick: () -> Unit = { },
    val onCancelClick: () -> Unit = { },
    val totalStickers: Int = 0,
    val album: Album = Album("", StickersList(emptyList()), AlbumStatus.Completing, ""),
    val isCreateAlbum: Boolean = true
)
