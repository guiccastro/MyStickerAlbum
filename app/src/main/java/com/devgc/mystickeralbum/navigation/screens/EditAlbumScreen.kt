package com.devgc.mystickeralbum.navigation.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devgc.mystickeralbum.R
import com.devgc.mystickeralbum.navigation.MainNavComponent
import com.devgc.mystickeralbum.navigation.interfaces.Screen
import com.devgc.mystickeralbum.scaffold.models.FABComponent
import com.devgc.mystickeralbum.scaffold.models.TopAppBarActionItem
import com.devgc.mystickeralbum.scaffold.models.TopAppBarComponent
import com.devgc.mystickeralbum.ui.screens.CreateEditAlbumUIScreen
import com.devgc.mystickeralbum.ui.viewmodels.CreateEditAlbumViewModel

object EditAlbumScreen : Screen {
    private lateinit var viewModel: CreateEditAlbumViewModel

    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.edit_album_title

        override fun hasMenu(): Boolean = CreateAlbumScreen.topAppBarComponent.hasMenu()

        override fun hasReturn(): Boolean = CreateAlbumScreen.topAppBarComponent.hasReturn()

        override fun getActionItems(): List<TopAppBarActionItem> =
            CreateAlbumScreen.topAppBarComponent.getActionItems()
    }
    override val fabComponent: FABComponent? = CreateAlbumScreen.fabComponent

    override fun NavGraphBuilder.screen() {
        composable(
            route = "$routeScreen/{${MainNavComponent.albumNameArgument}}",
            arguments = listOf(navArgument(MainNavComponent.albumNameArgument) {
                type = NavType.StringType
                nullable = true
            })
        ) {
            viewModel = hiltViewModel()
            CreateEditAlbumUIScreen(viewModel)
        }
    }

    override val routeScreen: String = "EditAlbumScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) =
        navigate("$routeScreen/${albumName}", navOptions)
}