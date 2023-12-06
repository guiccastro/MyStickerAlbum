package com.example.mystickeralbum.model

import androidx.annotation.StringRes

data class ButtonItem(
    @StringRes val text: Int,
    val onClick: () -> Unit
)
