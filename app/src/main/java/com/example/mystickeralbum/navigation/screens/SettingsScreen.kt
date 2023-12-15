package com.example.mystickeralbum.navigation.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mystickeralbum.R
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.scaffold.models.FABComponent
import com.example.mystickeralbum.scaffold.models.TopAppBarActionItem
import com.example.mystickeralbum.scaffold.models.TopAppBarComponent
import com.example.mystickeralbum.ui.screens.SettingsUIScreen

object SettingsScreen : Screen {

    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.drawer_item_settings_title

        override fun hasMenu(): Boolean = false

        override fun hasReturn(): Boolean = true

        override fun getActionItems(): List<TopAppBarActionItem> = emptyList()
    }

    override val fabComponent: FABComponent? = null

    override fun NavGraphBuilder.screen() {
        composable(routeScreen) {
            SettingsUIScreen()
        }
    }

    override val routeScreen: String = "SettingsScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) =
        navigate(routeScreen, navOptions)
}