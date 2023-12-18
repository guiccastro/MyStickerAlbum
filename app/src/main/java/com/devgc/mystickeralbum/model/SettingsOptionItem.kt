package com.devgc.mystickeralbum.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.navigation.interfaces.Screen
import com.devgc.mystickeralbum.navigation.screens.LanguageScreen

enum class SettingsOptionItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val screen: Screen
) {
    LanguageSettingItem(
        title = R.string.settings_language_title,
        icon = R.drawable.ic_language,
        screen = LanguageScreen
    )
}