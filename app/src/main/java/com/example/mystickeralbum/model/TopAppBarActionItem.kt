package com.example.mystickeralbum.model

import androidx.annotation.DrawableRes

data class TopAppBarActionItem(
    @DrawableRes val icon: Int,
    val onClick: () -> Unit
)
