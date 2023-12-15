package com.example.mystickeralbum.model

import androidx.annotation.StringRes
import com.example.mystickeralbum.model.TopAppBarActionItem

interface TopAppBarComponent {

    @StringRes
    fun getTitle(): Int

    fun hasMenu(): Boolean

    fun hasReturn(): Boolean

    fun getActionItems(): List<TopAppBarActionItem>
}