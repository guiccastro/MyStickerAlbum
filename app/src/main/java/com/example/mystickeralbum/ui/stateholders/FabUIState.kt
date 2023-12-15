package com.example.mystickeralbum.ui.stateholders

import androidx.annotation.DrawableRes
import com.example.mystickeralbum.R

data class FabUIState(
    @DrawableRes val icon: Int = R.drawable.ic_add,
    val hasFab: Boolean = false,
    val onClick: () -> Unit = {}
)
