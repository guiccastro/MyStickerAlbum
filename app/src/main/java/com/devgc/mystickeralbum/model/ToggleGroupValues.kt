package com.devgc.mystickeralbum.model

data class ToggleGroupValues(
    val options: List<String> = emptyList(),
    val onOptionClick: (Int) -> Unit = {},
    val selectedIndex: Int = 0
)
