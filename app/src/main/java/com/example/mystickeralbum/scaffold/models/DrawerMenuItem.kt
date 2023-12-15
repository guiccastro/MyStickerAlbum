package com.example.mystickeralbum.scaffold.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mystickeralbum.R
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.navigation.screens.AlbumsListScreen

enum class DrawerMenuItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val screen: Screen
) {
    SettingsDrawerMenuItem(
        title = R.string.drawer_item_settings_title,
        icon = R.drawable.ic_settings,
        screen = AlbumsListScreen
    ),

    AboutAppDrawerMenuItem(
        title = R.string.drawer_item_about_title,
        icon = R.drawable.ic_about_app,
        screen = AlbumsListScreen
    )
}