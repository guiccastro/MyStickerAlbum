package com.example.mystickeralbum.stateholders

import com.example.mystickeralbum.model.SpecialStickerType

data class AddAlbumUIState(
    val albumName: String = "",
    val onAlbumNameChange: (String) -> Unit = {},
    val albumImageUrl: String = "",
    val onAlbumImageUrlChange: (String) -> Unit = {},
    val normalStickersFrom: Int = 0,
    val onNormalStickersFromChange: (String) -> Unit = {},
    val normalStickersTo: Int = 0,
    val onNormalStickersToChange: (String) -> Unit = {},
    val hasSpecialStickers: Boolean = false,
    val onHasSpecialStickersChange: (Boolean) -> Unit = {},
    val specialStickerType: SpecialStickerType = SpecialStickerType.LetterNumber,
    val onSpecialStickerTypeChange: (SpecialStickerType) -> Unit = {},
    val specialStickersLetterFrom: String = "",
    val onSpecialStickersLetterFromChange: (String) -> Unit = {},
    val specialStickersLetterTo: String = "",
    val onSpecialStickersLetterToChange: (String) -> Unit = {},
    val specialStickersNumberFrom: Int = 0,
    val onSpecialStickersNumberFromChange: (String) -> Unit = {},
    val specialStickersNumberTo: Int = 0,
    val onSpecialStickersNumberToChange: (String) -> Unit = {},
    val onCreateClick: () -> Unit = {}
)
