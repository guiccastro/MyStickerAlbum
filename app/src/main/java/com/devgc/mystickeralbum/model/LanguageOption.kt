package com.devgc.mystickeralbum.model

import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R

enum class LanguageOption(
    @StringRes val title: Int,
    val localeOption: LocaleOption
) {
    EnglishOption(
        title = R.string.english_language_option,
        localeOption = LocaleOption.English
    ),
    PortugueseOption(
        title = R.string.portuguese_language_option,
        localeOption = LocaleOption.Portuguese
    )
}