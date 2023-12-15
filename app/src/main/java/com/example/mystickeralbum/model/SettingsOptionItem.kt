package com.example.mystickeralbum.model

import androidx.annotation.StringRes
import com.example.mystickeralbum.R
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.navigation.screens.LanguageScreen

enum class SettingsOptionItem(
    @StringRes val title: Int,
    val screen: Screen
) {
    LanguageSettingItem(
        title = R.string.settings_language_title,
        screen = LanguageScreen
    )
}