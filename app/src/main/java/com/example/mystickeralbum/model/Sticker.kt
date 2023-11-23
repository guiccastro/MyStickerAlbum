package com.example.mystickeralbum.model

data class Sticker(
    val identifier: String,
    val found: Boolean,
    val repeated: Int
) {
    val stickerType =
        if (identifier.toIntOrNull() == null) StickerType.Special else StickerType.Normal
}
