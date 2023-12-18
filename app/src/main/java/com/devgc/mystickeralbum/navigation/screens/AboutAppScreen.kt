package com.devgc.mystickeralbum.navigation.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.navigation.interfaces.Screen
import com.devgc.mystickeralbum.scaffold.models.FABComponent
import com.devgc.mystickeralbum.scaffold.models.TopAppBarActionItem
import com.devgc.mystickeralbum.scaffold.models.TopAppBarComponent
import com.devgc.mystickeralbum.ui.screens.AboutAppUIScreen

object AboutAppScreen : Screen {
    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.about_app_title

        override fun hasMenu(): Boolean = false

        override fun hasReturn(): Boolean = true

        override fun getActionItems(): List<TopAppBarActionItem> = emptyList()
    }

    override val fabComponent: FABComponent? = null

    override fun NavGraphBuilder.screen() {
        composable(routeScreen) {
            AboutAppUIScreen()
        }
    }

    override val routeScreen: String = "AboutAppScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) =
        navigate(routeScreen, navOptions)
}