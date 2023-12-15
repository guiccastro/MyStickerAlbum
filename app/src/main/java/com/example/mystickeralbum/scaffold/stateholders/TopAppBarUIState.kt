package com.example.mystickeralbum.scaffold.stateholders

import androidx.annotation.StringRes
import com.example.mystickeralbum.R
import com.example.mystickeralbum.scaffold.models.TopAppBarActionItem

data class TopAppBarUIState(
    @StringRes val title: Int = R.string.app_name,
    val hasMenu: Boolean = false,
    val hasReturn: Boolean = false,
    val onClickReturn: () -> Unit = {},
    val actionItems: List<TopAppBarActionItem> = emptyList()
)
