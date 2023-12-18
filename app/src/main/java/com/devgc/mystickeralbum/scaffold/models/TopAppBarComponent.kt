package com.devgc.mystickeralbum.scaffold.models

import androidx.annotation.StringRes

interface TopAppBarComponent {

    @StringRes
    fun getTitle(): Int

    fun hasMenu(): Boolean

    fun hasReturn(): Boolean

    fun getActionItems(): List<TopAppBarActionItem>
}