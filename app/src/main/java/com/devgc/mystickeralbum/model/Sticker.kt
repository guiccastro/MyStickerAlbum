package com.devgc.mystickeralbum.model

data class Sticker(
    val identifier: String,
    val found: Boolean,
    val repeated: Int,
    val lineBreakAfter: Boolean = false,
    val extraLineAfter: Boolean = false
) {
    val stickerType =
        if (identifier.toIntOrNull() == null) StickerType.Special else StickerType.Normal
}
