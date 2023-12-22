package com.devgc.mystickeralbum.model

import androidx.annotation.StringRes

data class ErrorValues(
    val hasError: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
