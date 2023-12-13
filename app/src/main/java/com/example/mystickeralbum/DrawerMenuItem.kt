package com.example.mystickeralbum

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mystickeralbum.activities.CreateEditAlbumActivity

enum class DrawerMenuItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val activityName: String
) {
    SettingsDrawerMenuItem(
        title = R.string.drawer_item_settings_title,
        icon = R.drawable.ic_settings,
        activityName = CreateEditAlbumActivity::class.java.name
    ),

    AboutAppDrawerMenuItem(
        title = R.string.drawer_item_about_title,
        icon = R.drawable.ic_about_app,
        activityName = CreateEditAlbumActivity::class.java.name
    )
}