package com.example.mystickeralbum.stateholders

import com.example.mystickeralbum.model.SpecialStickerType
import com.example.mystickeralbum.model.TextFieldValues

data class AddAlbumUIState(
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
    val onCreateClick: () -> Boolean = { false }
)
