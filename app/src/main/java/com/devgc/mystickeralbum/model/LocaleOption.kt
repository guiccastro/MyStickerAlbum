package com.devgc.mystickeralbum.model

import java.util.Locale

enum class LocaleOption(
    val locale: Locale
) {
    English(
        locale = Locale("en", "US")
    ),
    Portuguese(
        locale = Locale("pt", "BR")
    )
}