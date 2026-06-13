package com.devgc.mystickeralbum.scaffold.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TopAppBarActionItem(
    @DrawableRes val icon: Int,
    val menuItems: List<TopAppBarMenuItem> = emptyList(),
    val onClick: () -> Unit = {}
)

data class TopAppBarMenuItem(
    @StringRes val title: Int,
    val onClick: () -> Unit,
    val isSelected: () -> Boolean = { false }
)
