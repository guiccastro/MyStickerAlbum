package com.example.mystickeralbum.model

import androidx.annotation.StringRes
import com.example.mystickeralbum.R

data class TextFieldValues(
    val text: String = "",
    val onTextChange: (String) -> Unit = {},
    val error: Boolean = false,
    @StringRes val errorMessage: Int = R.string.error_empty_text_field
)
