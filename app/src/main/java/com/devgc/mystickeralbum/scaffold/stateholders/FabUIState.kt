package com.devgc.mystickeralbum.scaffold.stateholders

import androidx.annotation.DrawableRes
import com.devgc.mystickeralbum.R

data class FabUIState(
    @DrawableRes val icon: Int = R.drawable.ic_add,
    val hasFab: Boolean = false,
    val onClick: () -> Unit = {}
)
