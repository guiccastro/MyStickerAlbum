package com.devgc.mystickeralbum.model

data class CheckboxValues(
    val checked: Boolean = false,
    val onCheckedChange: (Boolean) -> Unit = {}
)
