package com.example.mystickeralbum.model

import androidx.annotation.DrawableRes

data class TopBarItem(
    @DrawableRes val image: Int,
    val onClick: () -> Unit
)
