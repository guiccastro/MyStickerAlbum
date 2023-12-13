package com.example.mystickeralbum.model

import androidx.annotation.StringRes

data class TopBarState(
    @StringRes val title: Int,
    val onReturn: (() -> Unit)? = null,
    val itemsList: List<TopBarItem> = emptyList()
)
