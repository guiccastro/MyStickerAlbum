package com.example.mystickeralbum.navigation.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mystickeralbum.R
import com.example.mystickeralbum.navigation.MainNavComponent
import com.example.mystickeralbum.navigation.interfaces.Screen
import com.example.mystickeralbum.scaffold.models.FABComponent
import com.example.mystickeralbum.scaffold.models.TopAppBarActionItem
import com.example.mystickeralbum.scaffold.models.TopAppBarComponent
import com.example.mystickeralbum.ui.screens.CreateEditAlbumUIScreen
import com.example.mystickeralbum.ui.viewmodels.CreateEditAlbumViewModel

object CreateAlbumScreen : Screen {

    private lateinit var viewModel: CreateEditAlbumViewModel

    override val topAppBarComponent: TopAppBarComponent = object : TopAppBarComponent {
        override fun getTitle(): Int = R.string.create_album_title

        override fun hasMenu(): Boolean = false

        override fun hasReturn(): Boolean = true

        override fun getActionItems(): List<TopAppBarActionItem> = emptyList()
    }

    override val fabComponent: FABComponent? = null

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

    override val routeScreen: String = "CreateAlbumScreen"

    override fun NavController.navigateToItself(albumName: String?, navOptions: NavOptions?) =
        navigate("$routeScreen/${albumName}", navOptions)
}