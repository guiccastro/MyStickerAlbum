package com.devgc.mystickeralbum.scaffold.stateholders

import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.scaffold.models.TopAppBarActionItem

data class TopAppBarUIState(
    @StringRes val title: Int = R.string.app_name,
    val hasMenu: Boolean = false,
    val hasReturn: Boolean = false,
    val onClickReturn: () -> Unit = {},
    val actionItems: List<TopAppBarActionItem> = emptyList()
)
