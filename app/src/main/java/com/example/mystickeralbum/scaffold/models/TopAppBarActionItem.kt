package com.example.mystickeralbum.scaffold.models

import androidx.annotation.DrawableRes

data class TopAppBarActionItem(
    @DrawableRes val icon: Int,
    val onClick: () -> Unit
)
